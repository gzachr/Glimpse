package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var currUser: User
    private lateinit var currUserUID: String
    private lateinit var userIDFilter: String
    private lateinit var postsQuery: Query
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userIDFilter = intent.getStringExtra(IntentKeys.USER_ID_FILTER.toString()) ?: "Everyone"

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.recyclerViewPosts

        auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            auth.currentUser?.let { firebaseUser ->
                val query = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
                query.addOnSuccessListener { querySnapshot ->
                    val userDocument = querySnapshot.documents.firstOrNull()
                    userDocument?.let {
                        currUser = it.toObject(User::class.java)!!
                        currUserUID = it.id
                        fetchPosts()
                    }
                }
            }
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

    private fun fetchPosts() {
        currUser.friendList += currUserUID
        if (userIDFilter != "Everyone") {
            postsQuery= FirestoreReferences.getPostCollectionReference()
                .whereEqualTo("userId", userIDFilter)
                .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        } else {
            if (currUser.friendList.isNotEmpty()) {
                postsQuery= FirestoreReferences.getPostCollectionReference()
                    .whereIn("userId", currUser.friendList)
                    .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
            } else {
                postsQuery = FirestoreReferences.getPostCollectionReference()
                    .whereEqualTo("nonExistentField", "nonExistentValue")
            }
        }

        postsQuery.get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    binding.noPostsGalleryTextView.visibility = View.VISIBLE
                    binding.noPostsGalleryTextView.text = "No posts yet."
                } else {
                    val options = FirestoreRecyclerOptions.Builder<Post>()
                        .setQuery(postsQuery, Post::class.java)
                        .build()

                    binding.noPostsGalleryTextView.visibility = View.GONE
                    adapter = GalleryAdapter(options, this)
                    binding.recyclerViewPosts.adapter = adapter
                    adapter.startListening()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreQuery", "Error retrieving posts: ${exception.message}")
            }
    }

    override fun onPostClick(postID: String) {
        val intent = Intent().apply {
            putExtra(IntentKeys.POST_ID.toString(), postID)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}