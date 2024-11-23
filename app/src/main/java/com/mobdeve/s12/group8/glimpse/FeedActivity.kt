package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFeedBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var usernameFilter: String
    private lateinit var postsQuery: Query
    private lateinit var binding: ActivityFeedBinding
    private lateinit var recyclerView: RecyclerView
    private var helper = LinearSnapHelper()
    private lateinit var adapter: FeedAdapter
    private val PREFS_NAME = "MyPrefs"
    private val TOAST_SHOWN_KEY = "toastShown"

    private val newIntentActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val postID = result.data?.getStringExtra(IntentKeys.POST_ID.toString())
            if (postID != null) {
                scrollToPost(postID) // Scroll to the chosen post
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usernameFilter = intent.getStringExtra(IntentKeys.USERNAME_FILTER.toString()) ?: "none"

        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv

        if (usernameFilter != "none") {
            postsQuery= FirestoreReferences.getPostCollectionReference()
                .whereEqualTo("userId", usernameFilter)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        } else {
            postsQuery= FirestoreReferences.getPostCollectionReference()
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        }

        val options = FirestoreRecyclerOptions.Builder<Post>()
            .setQuery(postsQuery, Post::class.java)
            .build()

        postsQuery.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    binding.noPostsTextView.visibility = View.VISIBLE
                } else {
                    binding.noPostsTextView.visibility = View.GONE
                    adapter = FeedAdapter(options)
                    recyclerView.adapter = adapter
                    adapter.startListening()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error retrieving posts: ${exception.message}")
            }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        helper.attachToRecyclerView(recyclerView)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val toastShown = sharedPreferences.getBoolean(TOAST_SHOWN_KEY, false)

        if (!toastShown) {
            Toast.makeText(this, "Double-tap post to see location!", Toast.LENGTH_LONG).show()
            with(sharedPreferences.edit()) {
                putBoolean(TOAST_SHOWN_KEY, true)
                apply()
            }
        }

        binding.locationBtn.setOnClickListener {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val snapView = helper.findSnapView(layoutManager) // Find the snapped view

            if (snapView != null) {
                val position = layoutManager.getPosition(snapView) // Get position of snapped view
                val adapter = recyclerView.adapter as FeedAdapter
                val currentPost = adapter.getItem(position) // Retrieve the post at this position
                val location = currentPost.location

                if (location.latitude != null && location.longitude != null) {
                    val intent = Intent(this, LocationActivity::class.java)
                    intent.putExtra("LATITUDE", location.latitude)
                    intent.putExtra("LONGITUDE", location.longitude)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Location not available for this post.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No post selected.", Toast.LENGTH_SHORT).show()
            }

        }

        binding.feedGalleryBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java).apply {
                putExtra(IntentKeys.USERNAME_FILTER.toString(), usernameFilter)
            }
            newIntentActivity.launch(intent)
        }

        binding.feedProfileBtn.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.feedNotificationsBtn.setOnClickListener {
            val intent = Intent(this, ReactionActivity::class.java)
            startActivity(intent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            finish()
        }

        binding.feedFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun scrollToPost(postID: String) {
        for (position in 0 until adapter.itemCount) {
            val snapshot = adapter.snapshots.getSnapshot(position)
            if (snapshot.id == postID) {
                (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
                break
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}
