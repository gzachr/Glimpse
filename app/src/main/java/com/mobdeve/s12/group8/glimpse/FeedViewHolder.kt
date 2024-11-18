package com.mobdeve.s12.group8.glimpse

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import OldPost
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FeedViewHolder(private val binding: FeedLayoutBinding): ViewHolder(binding.root) {
    private var stateFlag: Boolean = false
    private val imageUri: Uri = Uri.parse("https://todocodigo.net/img/626.jpg")
    private var mapFragment: SupportMapFragment? = null

    @SuppressLint("ClickableViewAccessibility")
    fun bind(oldPost: OldPost) {
        if (oldPost.username != "user1") {
            binding.deleteBtn.visibility = View.GONE
        }

        Glide.with(binding.feedUserIv.context)
            .load(oldPost.userImageId)
            .apply(RequestOptions().transform(RoundedCorners(50)))
            .into(binding.feedUserIv)

        // set height = to width so that image will show up as square
        binding.feedCv.post {
            val width = binding.feedCv.width
            val layoutParams = binding.feedCv.layoutParams
            layoutParams.height = width
            binding.feedCv.layoutParams = layoutParams
        }

        Glide.with(binding.feedPostIv.context)
            .load(oldPost.postImageId)
            .apply(RequestOptions().transform(RoundedCorners(16)))
            .into(binding.feedPostIv)

        binding.feedUsernameTv.text = oldPost.username
        binding.feedCreatedAtTv.text = oldPost.createdAt
        binding.feedCaptionTv.text = oldPost.caption

        //on double tap change to location view
        val gestureDetector =
            GestureDetector(binding.root.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    toggleMap()

                    // Start the image change with animation
//                    animateImageChange(binding.feedPostIv, newImage)
//                    stateFlag = !stateFlag

                    return true
                }
            })

//        binding.feedCv.setOnTouchListener { _, motionEvent ->
//            gestureDetector.onTouchEvent(motionEvent)
//            true
//        }

        binding.transparentOverlay.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            Log.d("TEST", "framelayout is clicked")
            true
        }

    }

    private fun toggleMap() {
        val fragmentManager = (binding.root.context as FragmentActivity).supportFragmentManager

        if (stateFlag) {
            // Show the image and hide/remove the map fragment
            binding.feedPostIv.visibility = View.VISIBLE
            binding.mapContainer.visibility = View.GONE

            mapFragment?.let {
                fragmentManager.beginTransaction().hide(it).commit()
            }

        } else {
            binding.feedPostIv.visibility = View.GONE
            binding.mapContainer.visibility = View.VISIBLE
            //TODO: adjust with actual post coordinates
            val marker = LatLng(14.56494073682104, 120.99320813598213)

            // Only create a new SupportMapFragment if one doesn't exist
            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance().also { fragment ->
                    fragment.getMapAsync { googleMap ->
                        // Customize the map, add markers, etc.
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(marker)
                                .title("Marker")
                        )
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker, 15f)
                        googleMap.moveCamera(cameraUpdate)
                        googleMap.uiSettings.setAllGesturesEnabled(false)
                    }
                    fragmentManager.beginTransaction().replace(binding.mapContainer.id, fragment)
                        .commit()
                }
            }
            else {
                // Show the map fragment if it already exists
                fragmentManager.beginTransaction().show(mapFragment!!).commit()
            }

        }
        stateFlag = !stateFlag
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