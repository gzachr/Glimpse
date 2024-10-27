package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobdeve.s12.group8.glimpse.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                ContextCompat.getColor(this, R.color.primary_pink),
                ContextCompat.getColor(this, R.color.primary_purple),
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.black),
                ContextCompat.getColor(this, R.color.black),
                )
        )

        binding.root.background = gradientDrawable


    }
}

