package com.mobdeve.s12.group8.glimpse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.databinding.ActivityProfileEditBinding
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private var selectedImageUri: Uri? = null
    private var cameraImageUri: Uri? = null
    private var isImageSelected = false

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
        private const val REQUEST_IMAGE_CAPTURE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.profileEditExitButton.setOnClickListener {
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val email = currentUser?.email
                if (email != null) {
                    val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                    if (!userSnapshot.isEmpty) {
                        val userDoc = userSnapshot.documents[0]
                        val currentUsername = userDoc.getString(FirestoreReferences.USERNAME_FIELD)
                        val currentProfileImageUrl = userDoc.getString(FirestoreReferences.PROFILE_IMAGE_URL_FIELD)

                        // Display current username and profile image
                        binding.editUsername.setText(currentUsername)
                        Glide.with(this@ProfileEditActivity)
                            .load(currentProfileImageUrl ?: R.drawable.user1)
                            .apply(RequestOptions().transform(RoundedCorners(1000)))
                            .into(binding.currentProfileImage)
                    }
                }
            } catch (e: Exception) {
                Log.e("EditActivity", "Failed to fetch user data: $e")
            }
        }

        binding.profileEditSaveButton.setOnClickListener {
            val newUsername = binding.editUsername.text.toString().trim()
            val newPassword = binding.usernameEt.text.toString().trim()

            if (newPassword.isNotEmpty() && newPassword.length < 6) {
                Toast.makeText(this, "Password should be minimum 6 characters!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val usernameDoc = FirestoreReferences.getUserByUsername(newUsername).await()

                    if (!usernameDoc.isEmpty) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@ProfileEditActivity, "Username Taken", Toast.LENGTH_LONG).show()
                        }
                        return@launch
                    }

                    var passwordUpdateSuccessful = true

                    val email = auth.currentUser?.email
                    if (email != null) {
                        val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                        if (!userSnapshot.isEmpty) {
                            val userDoc = userSnapshot.documents[0]
                            val userId = userDoc.id

                            if (newUsername.isNotEmpty()) {
                                FirestoreReferences.updateUser(userId, mapOf(FirestoreReferences.USERNAME_FIELD to newUsername)).await()
                            }

                            if (newPassword.isNotEmpty()) {
                                val passwordUpdateDeferred = CompletableDeferred<Boolean>()

                                currentUser?.updatePassword(newPassword)
                                    ?.addOnCompleteListener { updatePasswordTask ->
                                        CoroutineScope(Dispatchers.Main).launch {
                                            if (updatePasswordTask.isSuccessful) {
                                                try {
                                                    FirestoreReferences.updateUserPassword(userId, newPassword).await()
                                                    passwordUpdateDeferred.complete(true)
                                                    Toast.makeText(
                                                        this@ProfileEditActivity,
                                                        "Password updated successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } catch (e: Exception) {
                                                    Log.e("ProfileEditActivity", "Failed to update password in Firestore: $e")
                                                    Toast.makeText(
                                                        this@ProfileEditActivity,
                                                        "Password updated in Firebase, but failed to update Firestore.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    passwordUpdateDeferred.complete(false)
                                                }
                                            } else {
                                                Toast.makeText(
                                                    this@ProfileEditActivity,
                                                    "Failed to update password: ${updatePasswordTask.exception?.message}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                passwordUpdateDeferred.complete(false)
                                            }
                                        }
                                    }

                                passwordUpdateSuccessful = passwordUpdateDeferred.await()
                            }

                            if (!passwordUpdateSuccessful) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@ProfileEditActivity, "Profile update failed due to password error.", Toast.LENGTH_LONG).show()
                                }
                                return@launch
                            }
                            
                            selectedImageUri?.let { uri ->
                                val imageData = contentResolver.openInputStream(uri)?.readBytes()
                                imageData?.let {
                                    val downloadUrl = FirestoreReferences.uploadProfileImage(userId, it).await()
                                    FirestoreReferences.updateProfileImageUrl(userId, downloadUrl.toString()).await()
                                }
                            }

                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@ProfileEditActivity, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                                isImageSelected = false
                                finish()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ProfileEditActivity", "Error updating profile: $e")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ProfileEditActivity, "Failed to update profile.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        binding.profileEditImage.setOnClickListener {
            showImageSourceDialog()
        }

        Glide.with(this)
            .load(R.drawable.user1)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.currentProfileImage)

        Glide.with(this)
            .load(R.drawable.upload_profile_photo)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.profileEditImage)
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Update Profile Picture")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openCamera()
                1 -> openGallery()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        val imageFile = createImageFile()
        cameraImageUri = Uri.fromFile(imageFile)
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cameraImageUri)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun createImageFile(): File {
        val storageDir = externalCacheDir
        return File.createTempFile("profile_", ".jpg", storageDir)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    selectedImageUri = data?.data
                    selectedImageUri?.let { updateProfileImagePreview(it) }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    cameraImageUri?.let {
                        selectedImageUri = it
                        updateProfileImagePreview(it)
                    }
                }
            }
        }
    }

    private fun updateProfileImagePreview(imageUri: Uri) {
        isImageSelected = true
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.currentProfileImage)
    }

    override fun onResume() {
        super.onResume()

        if (!isImageSelected) {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val email = currentUser.email
                        if (!email.isNullOrEmpty()) {
                            val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                            if (!userSnapshot.isEmpty) {
                                val userDoc = userSnapshot.documents[0]
                                val currentUsername = userDoc.getString(FirestoreReferences.USERNAME_FIELD)
                                val currentProfileImageUrl = userDoc.getString(FirestoreReferences.PROFILE_IMAGE_URL_FIELD)

                                binding.editUsername.setText(currentUsername)
                                Glide.with(this@ProfileEditActivity)
                                    .load(currentProfileImageUrl ?: R.drawable.user1)
                                    .apply(RequestOptions().transform(CircleCrop()))
                                    .into(binding.currentProfileImage)
                            } else {
                                Log.e("ProfileEditActivity", "User data not found for email: $email")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("ProfileEditActivity", "Error refreshing user data: $e")
                    }
                }
            } else {
                Log.e("ProfileEditActivity", "No logged-in user.")
            }
        }
    }
}
