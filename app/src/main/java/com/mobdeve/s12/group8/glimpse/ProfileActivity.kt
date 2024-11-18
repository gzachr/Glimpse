package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileExitButton.setOnClickListener{
            finish()
        }

        binding.profileEditButton.setOnClickListener {
            val intent = Intent(applicationContext, ProfileEditActivity::class.java)
            startActivity(intent)
        }
        binding.profileEditLabel.setOnClickListener{
            val intent = Intent(applicationContext, ProfileEditActivity::class.java)
            startActivity(intent)
        }
        Glide.with(this)
            .load(R.drawable.user1)
            .apply(RequestOptions().transform(RoundedCorners(1000)))
            .into(binding.profileImage)

        binding.profileLogoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finishAffinity()
        }
    }
}