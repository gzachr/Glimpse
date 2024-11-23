package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import OldPost
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import com.mobdeve.s12.group8.glimpse.model.OldReaction
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var usernameFilter: String
    private lateinit var postsQuery: Query
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usernameFilter = intent.getStringExtra(IntentKeys.USERNAME_FILTER.toString()) ?: "none"

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerViewPosts

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
                    binding.noPostsGalleryTextView.visibility = View.VISIBLE
                } else {
                    binding.noPostsGalleryTextView.visibility = View.GONE
                    adapter = GalleryAdapter(options, this)
                    binding.recyclerViewPosts.adapter = adapter
                    adapter.startListening()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error retrieving posts: ${exception.message}")
            }

        recyclerView.layoutManager = GridLayoutManager(this, 3)

        binding.galleryProfileButton.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.galleryNotificationsBtn.setOnClickListener {
            val intent = Intent(this, ReactionActivity::class.java)
            startActivity(intent)
        }

        binding.galleryReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }

            setResult(RESULT_OK, resultIntent)
            startActivity(resultIntent)
            finish()
        }

        binding.galleryFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPostClick(postID: String) {
        val intent = Intent().apply {
            putExtra(IntentKeys.POST_ID.toString(), postID)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
    }
}