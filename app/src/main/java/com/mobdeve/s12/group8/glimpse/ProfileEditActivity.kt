package com.mobdeve.s12.group8.glimpse

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
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditExitButton.setOnClickListener{
            finish()
        }
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Fetch and display the current username
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val email = currentUser?.email
                if (email != null) {
                    val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                    if (!userSnapshot.isEmpty) {
                        val userDoc = userSnapshot.documents[0]
                        val currentUsername = userDoc.getString(FirestoreReferences.USERNAME_FIELD)
                        binding.editUsername.setText(currentUsername)
                    }
                }
            } catch (e: Exception) {
                Log.e("EditActivity", "Failed to fetch user data: $e")
            }
        }

        // Handle Save button click
        binding.profileEditSaveButton.setOnClickListener {
            val newUsername = binding.editUsername.text.toString().trim()
            val newPassword = binding.usernameEt.text.toString().trim()

            // Validate input
            if (newUsername.isEmpty() && newPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill out at least one field to update",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val email = currentUser?.email
                    if (email != null) {
                        // Update in Firestore
                        if (newUsername.isNotEmpty()) {
                            val userSnapshot = FirestoreReferences.getUserByEmail(email).await()
                            val userDoc = userSnapshot.documents[0]
                            val userId = userDoc.id
                            FirestoreReferences.updateUser(
                                userId, mapOf(FirestoreReferences.USERNAME_FIELD to newUsername)
                            ).await()
                        }

                        // Update password in Firebase Auth directly
                        if (newPassword.isNotEmpty()) {
                            currentUser.updatePassword(newPassword)
                                .addOnCompleteListener { updatePasswordTask ->
                                    if (updatePasswordTask.isSuccessful) {
                                        Toast.makeText(
                                            this@ProfileEditActivity,
                                            "Password updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        finish() // Close activity
                                    } else {
                                        Toast.makeText(
                                            this@ProfileEditActivity,
                                            "Failed to update password: ${updatePasswordTask.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("EditActivity", "Error updating details: $e")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ProfileEditActivity,
                            "Failed to update details: $e",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        Glide.with(this)
            .load(R.drawable.user1)
            .apply(RequestOptions().transform(RoundedCorners(1000)))
            .into(binding.currentProfileImage)

        Glide.with(this)
            .load(R.drawable.upload_profile_photo)
            .apply(RequestOptions()
                .transform(CircleCrop()))
            .into(binding.profileEditImage)

    }
}