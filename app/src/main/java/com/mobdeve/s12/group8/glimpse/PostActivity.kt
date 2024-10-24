package com.mobdeve.s12.group8.glimpse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.group8.glimpse.databinding.ActivityPostBinding

class PostActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postIv.post {
            val width = binding.postIv.width
            val layoutParams = binding.postIv.layoutParams
            layoutParams.height = width
            binding.postIv.layoutParams = layoutParams
        }

        binding.postBtn.setOnClickListener {
            /**
             * TODO posting logic, save to DB
             */

            finish()
        }

        binding.postExitBtn.setOnClickListener {
            finish()
        }
    }
}