package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobdeve.s12.group8.glimpse.databinding.ActivityMainBinding
import android.Manifest
import androidx.core.app.ActivityCompat
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("toastShown", false)
            apply()
        }

        checkCameraPermission()

        if(auth.currentUser != null) {
            // navigate to main activity already
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
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

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission()
        }
    }

    private fun checkLocationPermission(){

    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission is required to use this application.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

