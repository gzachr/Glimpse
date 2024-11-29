package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.databinding.ActivityProfileBinding
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.profileExitButton.setOnClickListener{
            finish()
        }

        val auth = FirebaseAuth.getInstance()
        val email = auth.currentUser?.email

        if (email != null) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val userSnapshot = FirestoreReferences.getUserByEmail(email).await()

                    if (!userSnapshot.isEmpty) {
                        val userDocument = userSnapshot.documents[0]
                        val username = userDocument.getString(FirestoreReferences.USERNAME_FIELD)
                        val profileImageUrl = userDocument.getString(FirestoreReferences.PROFILE_IMAGE_URL_FIELD)

                        //show info from db
                        binding.profileName.text = username
                        binding.profileEmail.text = email

                        if (!profileImageUrl.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity)
                                .load(profileImageUrl)
                                .apply(RequestOptions().transform(CircleCrop()))
                                .into(binding.profileImage)
                        } else {
                            //default pfp load
                            FirestoreReferences.getDefaultUserPhoto()
                                .addOnSuccessListener { uri ->
                                    Glide.with(this@ProfileActivity)
                                        .load(uri)
                                        .apply(RequestOptions().transform(CircleCrop()))
                                        .into(binding.profileImage)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("ProfileActivity", "Failed to load default user photo: $exception")
                                }
                        }
                    } else {
                        Log.e("ProfileActivity", "User not found with email: $email")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileActivity", "Error fetching user data: $e")
                }
            }
        } else {
            Log.e("ProfileActivity", "No logged-in user found.")
        }

        binding.profileEditButton.setOnClickListener {
            val intent = Intent(applicationContext, ProfileEditActivity::class.java)
            intent.putExtra("originalUsername", binding.profileName.toString())
            startActivity(intent)
        }

        binding.profileLogoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
    //automatic changes from editProfile
    override fun onResume() {
        super.onResume()

        val email = auth.currentUser?.email

        if (email != null) {
            binding.profileDetailsCl.visibility = View.INVISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val userSnapshot = FirestoreReferences.getUserByEmail(email).await()

                    if (!userSnapshot.isEmpty) {
                        val userDocument = userSnapshot.documents[0]
                        val username = userDocument.getString(FirestoreReferences.USERNAME_FIELD)
                        val profileImageUrl = userDocument.getString(FirestoreReferences.PROFILE_IMAGE_URL_FIELD)


                        binding.profileName.text = username
                        binding.profileEmail.text = email

                        if (!profileImageUrl.isNullOrEmpty()) {
                            Glide.with(this@ProfileActivity)
                                .load(profileImageUrl)
                                .apply(RequestOptions().transform(CircleCrop()))
                                .into(binding.profileImage)
                        } else {
                            FirestoreReferences.getDefaultUserPhoto()
                                .addOnSuccessListener { uri ->
                                    Glide.with(this@ProfileActivity)
                                        .load(uri)
                                        .apply(RequestOptions().transform(CircleCrop()))
                                        .into(binding.profileImage)
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("ProfileActivity", "Failed to load default user photo: $exception")
                                }
                        }
                    } else {
                        Log.e("ProfileActivity", "User not found with email: $email")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileActivity", "Error refreshing user data: $e")
                } finally {
                    binding.profileDetailsCl.visibility = View.VISIBLE
                }
            }
        } else {
            Log.e("ProfileActivity", "No logged-in user found.")
        }
    }
}