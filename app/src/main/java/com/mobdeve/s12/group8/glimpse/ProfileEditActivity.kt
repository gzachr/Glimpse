package com.mobdeve.s12.group8.glimpse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.databinding.ActivityProfileEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private var isImageSelected: Boolean = false
    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        binding.profileEditExitButton.setOnClickListener {
            finish()
        }

        // Fetch and display the current username
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

            if (newUsername.isEmpty() && newPassword.isEmpty() && selectedImageUri == null) {
                Toast.makeText(this, "Please update at least one field.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val email = auth.currentUser?.email
                    if (email != null) {
                        val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                        if (!userSnapshot.isEmpty) {
                            val userDoc = userSnapshot.documents[0]
                            val userId = userDoc.id

                            // Update username
                            if (newUsername.isNotEmpty()) {
                                FirestoreReferences.updateUser(userId, mapOf(FirestoreReferences.USERNAME_FIELD to newUsername)).await()
                            }

                            // Update password
                            if (newPassword.isNotEmpty()) {
                                auth.currentUser?.updatePassword(newPassword)?.await()
                            }

                            // Upload profile image if selected
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
        Glide.with(this)
            .load(R.drawable.user1)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.currentProfileImage)

        Glide.with(this)
            .load(R.drawable.upload_profile_photo)
            .apply(RequestOptions()
                .transform(CircleCrop()))
            .into(binding.profileEditImage)
    }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri = result.data?.data
                if (selectedImageUri != null) {
                    updateProfileImagePreview(selectedImageUri!!)
                } else {
                    Log.e("EditActivity", "No image selected or URI is null.")
                }
            }
        }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun updateProfileImagePreview(imageUri: Uri) {
        // Update the profile image preview
        isImageSelected = true
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.currentProfileImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            binding.currentProfileImage.setImageURI(uri) // Display the selected image
            uri?.let {
                selectedImageUri = it
                isImageSelected = true
            }
        }
        Glide.with(this)
            .load(selectedImageUri)
            .apply(RequestOptions().transform(CircleCrop()))
            .into(binding.currentProfileImage)

    }
    override fun onResume() {
        super.onResume()

        // Only refresh data if no tentative image is selected
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

                                // Update username in EditText
                                binding.editUsername.setText(currentUsername)

                                // Load profile image (custom or default)
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
