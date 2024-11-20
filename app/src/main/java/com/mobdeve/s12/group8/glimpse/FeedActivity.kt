package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    private var isLiked = false
    private val PREFS_NAME = "MyPrefs"
    private val TOAST_SHOWN_KEY = "toastShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usernameFilter = intent.getStringExtra("filterUsername") ?: "none"

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
                    val adapter = FeedAdapter(options)
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

        binding.heartBtn.setOnClickListener {
            binding.heartBtn.setImageResource(if (isLiked) R.drawable.heart_icon_outline else R.drawable.heart_icon_filled)
            isLiked = !isLiked
        }

        binding.feedViewAllBtn.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        binding.feedProfileBtn.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.feedMessageBtn.setOnClickListener {
            val intent = Intent(this, ReactionActivity::class.java)
            startActivity(intent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.feedFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            startActivity(intent)
        }
    }
}
