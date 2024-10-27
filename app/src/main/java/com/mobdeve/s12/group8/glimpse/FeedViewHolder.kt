package com.mobdeve.s12.group8.glimpse

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import Post
import com.bumptech.glide.request.target.Target

class FeedViewHolder(private val binding: FeedLayoutBinding): ViewHolder(binding.root) {
    private var stateFlag: Boolean = false
    private val imageUri: Uri = Uri.parse("https://todocodigo.net/img/626.jpg")

    @SuppressLint("ClickableViewAccessibility")
    fun bind(post: Post) {
        if (post.username != "user1") {
            binding.deleteBtn.visibility = View.GONE
        }

        Glide.with(binding.feedUserIv.context)
            .load(post.userImageId)
            .apply(RequestOptions().transform(RoundedCorners(50)))
            .into(binding.feedUserIv)

        // set height = to width so that image will show up as square
        binding.feedPostIv.post {
            val width = binding.feedPostIv.width
            val layoutParams = binding.feedPostIv.layoutParams
            layoutParams.height = width
            binding.feedPostIv.layoutParams = layoutParams
        }

        Glide.with(binding.feedPostIv.context)
            .load(post.postImageId)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(binding.feedPostIv)

        binding.feedUsernameTv.text = post.username
        binding.feedCreatedAtTv.text = post.createdAt
        binding.feedCaptionTv.text = post.caption

        //on double tap change to location view
        val gestureDetector =
            GestureDetector(binding.root.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val newImage = if (stateFlag) {
                        post.postImageId // Use the original image
                    } else {
                        R.drawable.map // Use the map image
                    }

                    // Start the image change with animation
                    animateImageChange(binding.feedPostIv, newImage)
                    stateFlag = !stateFlag

                    return true
                }
            })

        binding.feedCv.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }
    }

    private fun animateImageChange(imageView: ImageView, imageResId: Int) {
        // Create a fade-out animation
        val fadeOut = ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f).setDuration(300)
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Load the new image once fade out is done
                Glide.with(imageView.context)
                    .load(imageResId)
                    .apply(RequestOptions().transform(RoundedCorners(16)))
                    .into(imageView)

                // Create a fade-in animation
                val fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f).setDuration(300)
                fadeIn.start() // Start the fade-in animation
            }
        })

        fadeOut.start() // Start the fade-out animation
    }

    fun setDeleteButtonListener(listener: View.OnClickListener) {
        binding.deleteBtn.setOnClickListener(listener) // Set the click listener for delete button
    }
}