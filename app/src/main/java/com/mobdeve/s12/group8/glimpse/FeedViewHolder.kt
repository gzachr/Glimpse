package com.mobdeve.s12.group8.glimpse

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import android.util.Log
import android.view.View.INVISIBLE
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.GeoPoint
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class FeedViewHolder(private val binding: FeedLayoutBinding): ViewHolder(binding.root) {
    private var stateFlag: Boolean = false
    private var mapFragment: SupportMapFragment? = null
    private var isLiked = false
    private var tempWidth: Int = 0

    @SuppressLint("ClickableViewAccessibility")
    fun bind(documentId: String, post: Post, user: User, currUserUID: String, currUserReactedPostsID: List<String>, isMapVisible: Boolean, toggleMap: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            // Bind map visibility based on state
            if (isMapVisible) {
                binding.feedPostIv.visibility = View.GONE
                binding.mapContainer.visibility = View.VISIBLE
                // Set up the map...
            } else {
                binding.feedPostIv.visibility = View.VISIBLE
                binding.mapContainer.visibility = View.GONE
            }

            // check if user is owner of post
            if (currUserUID != post.userId) {
                binding.deleteBtn.visibility = View.GONE
                isLiked = false
                binding.heartBtn.isEnabled = true
                binding.heartBtn.setImageResource(R.drawable.heart_icon_outline)
                for (reactedPostID in currUserReactedPostsID) {
                    if (reactedPostID == documentId) {
                        isLiked = true
                        binding.heartBtn.setImageResource(R.drawable.heart_icon_filled)
                        break
                    }
                }
            } else {
                binding.deleteBtn.visibility = View.VISIBLE
                binding.heartBtn.isEnabled = false
                binding.heartBtn.setImageResource(R.drawable.heart_icon_grey)
            }

            user.let {
                val profileImageUrl = it.profileImage
                val imageUrl = profileImageUrl ?: FirestoreReferences.getDefaultUserPhoto().result.toString()

                Glide.with(binding.feedUserIv.context)
                    .load(imageUrl)
                    .apply(RequestOptions().transform(RoundedCorners(50)))
                    .into(binding.feedUserIv)
            }

            val screenHeight = Resources.getSystem().displayMetrics.heightPixels

            // set height = to width so that image will show up as square
            binding.feedCv.post {
                //resize based on screen height
                val width = binding.feedCv.width
                if(tempWidth == 0)
                    tempWidth = width
                val layoutParams = binding.feedCv.layoutParams
                if(screenHeight < 2160) {
                    layoutParams.height = ceil(tempWidth*.8).toInt()
                    layoutParams.width = ceil(tempWidth*.8).toInt()
                } else {
                    layoutParams.height = tempWidth
                    layoutParams.width = tempWidth
                }
                binding.feedCv.layoutParams = layoutParams
            }

            Glide.with(binding.feedPostIv.context)
                .load(post.imgUri)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .into(binding.feedPostIv)

            if (user.username?.length!! > 11) {
                user.username = user.username!!.substring(0, 6) + "..."
            }

            binding.feedUsernameTv.text = user.username
            binding.feedCreatedAtTv.text = formatTimestampToRelative(post.createdAt)


            binding.feedCaptionTv.text = post.caption

            if(post.caption == "")
                binding.feedCaptionTv.visibility = INVISIBLE

            //on double tap change to location view
            val gestureDetector = GestureDetector(binding.root.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    toggleMap(post.location) // Use the `toggleMap` function with the post's GeoPoint
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
    }

    private fun toggleMap(geoPoint: GeoPoint) {
        val fragmentManager = (binding.root.context as FragmentActivity).supportFragmentManager

        if (stateFlag) {
            binding.feedPostIv.visibility = View.VISIBLE
            binding.mapContainer.visibility = View.GONE

            mapFragment?.let {
                fragmentManager.beginTransaction().hide(it).commit()
            }
        } else {
            binding.feedPostIv.visibility = View.GONE
            binding.mapContainer.visibility = View.VISIBLE

            val marker = LatLng(geoPoint.latitude, geoPoint.longitude)

            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance().also { fragment ->
                    fragment.getMapAsync { googleMap ->
                        // Configure map settings
                        googleMap.addMarker(MarkerOptions().position(marker).title("Marker"))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 15.5f))
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

    private fun formatTimestampToRelative(timestamp: Date?): String {
        if (timestamp == null) return "Unknown"

        val now = System.currentTimeMillis()
        val createdAt = timestamp.time
        val durationMillis = now - createdAt

        val days = TimeUnit.MILLISECONDS.toDays(durationMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60

        return when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "Just now"
        }
    }

    fun setReactButtonListener(documentId: String, currUserUID: String, userId: String) {
        binding.heartBtn.setOnClickListener {
            likePost(documentId, currUserUID, userId)
        }
    }

    private fun likePost(documentId: String, currUserUID: String, userId: String) {
        binding.heartBtn.setImageResource(if (isLiked) R.drawable.heart_icon_outline else R.drawable.heart_icon_filled)
        isLiked = !isLiked

        if (isLiked) { // liked a new post
            val reaction = Reaction(documentId, currUserUID)
            CoroutineScope(Dispatchers.Main).launch {
                FirestoreReferences.addReaction(reaction).await()

                val reactionsQuery = FirestoreReferences.getReactionCollectionReference()
                    .whereEqualTo("postId", documentId)
                    .whereEqualTo("reactorId", currUserUID)
                    .get()
                    .await()

                val reactionId = reactionsQuery.documents[0].id

                val userDocRef = FirestoreReferences.getUserByID(userId).await()
                userDocRef.reference.update("reactionsReceived", FieldValue.arrayUnion(reactionId))
                    .addOnSuccessListener {
                        Log.d("FeedAdapter", "Reaction added to user's reactionsReceived successfully")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("FeedAdapter", "Error updating reactionsReceived: ${exception.message}")
                    }
            }
        }
        else { // unliked a post
            FirestoreReferences.getReactionCollectionReference()
                .whereEqualTo("postId", documentId)
                .whereEqualTo("reactorId", currUserUID)
                .get()
                .addOnSuccessListener { querySnapshot ->

                    val reactionDoc = querySnapshot.documents.firstOrNull()

                    reactionDoc?.let { doc ->
                        doc.reference.delete()
                            .addOnSuccessListener {
                                Log.d("FeedAdapter", "Reaction deleted successfully")

                                val reactionId = doc.id // Get the reaction ID

                                FirestoreReferences.getUserByID(userId)
                                    .addOnSuccessListener { userDocSnapshot ->
                                        userDocSnapshot.reference.update(
                                            "reactionsReceived", FieldValue.arrayRemove(reactionId)
                                        )
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "FeedAdapter",
                                                    "Successfully removed reactionId from reactionsReceived"
                                                )
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e(
                                                    "FeedAdapter",
                                                    "Error removing reactionId from reactionsReceived: ${exception.message}"
                                                )
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e(
                                            "FeedAdapter",
                                            "Error fetching user document: ${exception.message}"
                                        )
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(
                                    "FeedAdapter",
                                    "Error deleting reaction: ${exception.message}"
                                )
                            }
                    } ?: run {
                        Log.d("FeedAdapter", "No reaction found to delete.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FeedAdapter", "Error querying reactions: ${exception.message}")
                }
        }
    }

    fun setDeleteButtonListener(documentId: String) {
        binding.deleteBtn.setOnClickListener {
            deletePost(documentId)
        }
    }

    private fun deletePost(documentId: String) {
        val postRef = FirestoreReferences.getPostCollectionReference().document(documentId)

        postRef.delete()
            .addOnSuccessListener {
                // Notify the user of successful deletion (optional)
                Log.d("FeedAdapter", "Post $documentId deleted successfully")
            }
            .addOnFailureListener { exception ->
                // Handle the error
                Log.e("FeedAdapter", "Error deleting post $documentId: ${exception.message}")
            }
    }
}