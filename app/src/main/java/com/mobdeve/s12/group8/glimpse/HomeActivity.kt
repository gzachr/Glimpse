package com.mobdeve.s12.group8.glimpse

import OldPost
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.Surface
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.mobdeve.s12.group8.glimpse.databinding.ActivityHomeBinding
import com.mobdeve.s12.group8.glimpse.model.OldReaction
import java.io.File

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var gestureDetector: GestureDetector
    private var posts = DataHelper.loadPostData()
    private var reactions = DataHelper.loadReactionData()
    private var lensFacing: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var firstToast = false
    private val REQUEST_CODE_FEED = 1
    private var imageCapture: ImageCapture? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!firstToast) {
            Toast.makeText(this, "Double-tap to switch camera!", Toast.LENGTH_LONG).show()
            firstToast = true
        }

        binding.viewFeedBtn.setOnClickListener {
            val intent = Intent(applicationContext, FeedActivity::class.java).apply {
                putExtra("data", posts)
                putExtra("reactions", reactions)
            }
            startActivityForResult(intent, REQUEST_CODE_FEED)  // Use startActivityForResult

        }

        binding.postExitBtn.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.notificationBtn.setOnClickListener {
            val intent = Intent(applicationContext, ReactionActivity::class.java)
            intent.putExtra("data", posts)
            intent.putExtra("reactions", reactions)
            startActivity(intent)
        }

        binding.friendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            intent.putExtra("data", posts)
            startActivity(intent)
        }

        binding.previewView.viewTreeObserver.addOnGlobalLayoutListener {
            // Get the width of the PreviewView (which is match_parent)
            val previewWidth = binding.previewView.width

            // Set the height equal to the width to make it square
            val layoutParams = binding.previewView.layoutParams
            layoutParams.height = previewWidth
            binding.previewView.layoutParams = layoutParams
        }

        // handle double tap gesture
        gestureDetector = GestureDetector(applicationContext, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                flipCamera() // Call your flipCamera function here
                return true
            }
        })

        // switch camera on double tap
        binding.previewView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }

        binding.captureBtn.setOnClickListener {
            val photoFile = File(externalMediaDirs.firstOrNull(), "${System.currentTimeMillis()}.jpg")
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture?.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object: ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        val intent = Intent(applicationContext, PostActivity::class.java).apply {
                            putExtra("CAPTURED_IMAGE_URI", savedUri.toString())
                        }
                        startActivity(intent)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Toast.makeText(applicationContext, "Failed to capture image", Toast.LENGTH_SHORT).show()
                        Log.e("HomeActivity", "Image capture failed", exception)
                    }
                }
            )
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

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(displayRotation)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            try {
                // Unbind any previous use cases
                cameraProvider.unbindAll()

                // Bind the camera lifecycle with the Preview use case
                cameraProvider.bindToLifecycle(this, lensFacing, preview, imageCapture)

            } catch (exc: Exception) {
                Log.e("CameraX", "Binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun flipCamera() {
        lensFacing = if (lensFacing == CameraSelector.DEFAULT_FRONT_CAMERA) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        startCamera() // Restart the camera with the new lensFacing
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_FEED && resultCode == RESULT_OK) {
            val updatedPosts = data?.getParcelableArrayListExtra<OldPost>("updated_posts")
            updatedPosts?.let {
                posts.clear()
                posts.addAll(it)

            }

            val updatedRequests = data?.getParcelableArrayListExtra<OldReaction>("updated_reactions")
            updatedRequests?.let{
                reactions.clear()
                reactions.addAll(it)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val checkFilter = intent.getStringExtra("usernameFilter") ?: "none"
        val checkFromGallery = intent.getIntExtra("fromGallery", -1)

        intent.let {
            val updatedPosts = it.getParcelableArrayListExtra<OldPost>("updated_posts")
            updatedPosts?.let { newPosts ->
                posts.clear()
                posts.addAll(newPosts)
            }
            val updatedReactions = it.getParcelableArrayListExtra<OldReaction>("updated_reactions")
            updatedReactions?.let { newReactions ->
                reactions.clear()
                reactions.addAll(newReactions)
            }
        }

        if (checkFromGallery == -1) {
            intent.let {
                val position = it.getIntExtra("open_feed_at_position", -1)
                val feedIntent = Intent(applicationContext, FeedActivity::class.java).apply {
                    putExtra("data", posts)
                    putExtra("reactions", reactions)
                    if (position != -1) {
                        putExtra("position", position)
                    }
                    if (checkFilter != "none") {
                        val filterUsername = it.getStringExtra("usernameFilter")
                        putExtra("filterUsername", filterUsername)
                    }
                }
                startActivityForResult(feedIntent, REQUEST_CODE_FEED)
            }
        }
    }
}