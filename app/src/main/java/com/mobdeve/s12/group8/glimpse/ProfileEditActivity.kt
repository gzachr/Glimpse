package com.mobdeve.s12.group8.glimpse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.ActivityProfileEditBinding

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileEditExitButton.setOnClickListener{
            finish()
        }
        binding.profileEditSaveButton.setOnClickListener{
            finish()
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