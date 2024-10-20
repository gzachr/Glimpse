package com.mobdeve.s12.group8.glimpse

import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.mobdeve.s12.group8.glimpse.databinding.ActivityHomeBinding

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.previewView.viewTreeObserver.addOnGlobalLayoutListener {
            // Get the width of the PreviewView (which is match_parent)
            val previewWidth = binding.previewView.width

            // Set the height equal to the width to make it square
            val layoutParams = binding.previewView.layoutParams
            layoutParams.height = previewWidth
            binding.previewView.layoutParams = layoutParams
        }

        startCamera()
    }

    private fun startCamera() {
        val displayRotation = binding.previewView.display?.rotation ?: Surface.ROTATION_0
        val preview = Preview.Builder()
            .setTargetRotation(displayRotation)
            .build()
            .also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind any previous use cases
                cameraProvider.unbindAll()

                // Bind the camera lifecycle with the Preview use case
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)

            } catch (exc: Exception) {
                Log.e("CameraX", "Binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
}