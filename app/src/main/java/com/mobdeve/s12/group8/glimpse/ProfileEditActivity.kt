package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}