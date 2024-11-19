package com.mobdeve.s12.group8.glimpse

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QuerySnapshot
import com.mobdeve.s12.group8.glimpse.databinding.ActivityPostBinding
import com.mobdeve.s12.group8.glimpse.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class PostActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var auth: FirebaseAuth
    private var croppedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postRootLayout.requestFocus()

        binding.postIv.post {
            val width = binding.postIv.width
            val layoutParams = binding.postIv.layoutParams
            layoutParams.height = width
            binding.postIv.layoutParams = layoutParams
        }

        //get captured photo from camera
        val imageUri = intent.getStringExtra("CAPTURED_IMAGE_URI")
        imageUri?.let {
            val uri = Uri.parse(it)

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val rotatedBitmap = bitmap.rotate(90f)

            croppedBitmap = rotatedBitmap.cropToSquare()

            Glide.with(this)
                .load(croppedBitmap)
                .centerInside()
                .into(binding.postIv)
        }

        binding.captionCounterTv.visibility = View.GONE

        //only show the caption counter if user is focused on it
        binding.captionEt.setOnFocusChangeListener{_, hasFocus ->
            if(hasFocus)
                binding.captionCounterTv.visibility = View.VISIBLE
            else
                binding.captionCounterTv.visibility = View.GONE
        }

        binding.captionEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the character count text view
                val currentLength = s?.length ?: 0
                binding.captionCounterTv.text = "$currentLength/60"
            }
        })

        binding.postBtn.setOnClickListener {
            savePost()
            finish()
        }

        binding.postExitBtn.setOnClickListener {
            finish()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && event.action == MotionEvent.ACTION_DOWN) {
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    // Hide keyboard and clear focus if clicking outside the EditText
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    // Extension function to rotate a Bitmap by a given number of degrees
    private fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    private fun Bitmap.cropToSquare(): Bitmap {
        val dimension = minOf(width, height)
        val xOffset = (width - dimension) / 2
        val yOffset = (height - dimension) / 2
        return Bitmap.createBitmap(this, xOffset, yOffset, dimension, dimension)
    }

    private fun savePost() {
        var postUri: Uri
        var res: QuerySnapshot

        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(auth.currentUser != null) {
                    val email = auth.currentUser!!.email
                    res = FirestoreReferences.getUserByEmail(email!!).await()
                    val uid = res.documents[0].id


                    croppedBitmap?.let { bitmap ->
                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                        val data = bytes.toByteArray()

                        postUri = FirestoreReferences.saveImageToStorage(uid, data).await()

                        //adjust geoPoint with current location
                        val location = GeoPoint(0.0, 0.0)
                        val post = Post(postUri.toString(), location, uid, binding.captionEt.text.toString())
                        FirestoreReferences.addPost(post).await()
                    }
                }

            } catch(e: Exception) {
                Log.e("POST_ACTIVITY", "Error creating post")
            }

        }
    }
}