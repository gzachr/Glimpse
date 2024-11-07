package com.mobdeve.s12.group8.glimpse

import Post
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFeedBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class FeedActivity : AppCompatActivity(), FeedAdapter.PostDeleteCallback {
    private var posts: ArrayList<Post> = ArrayList()
    private var filteredPosts: ArrayList<Post> = ArrayList()
    private var reactions: ArrayList<Reaction> = ArrayList()
    private lateinit var usernameFilter: String
    private lateinit var binding: ActivityFeedBinding
    private lateinit var recyclerView: RecyclerView
    private var helper = LinearSnapHelper()
    private var isLiked = false
    private val PREFS_NAME = "MyPrefs"
    private val TOAST_SHOWN_KEY = "toastShown"

    private val newIntentActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val position = result.data?.getIntExtra("position", -1)
            position?.let {
                if (it >= 0) {
                    if (usernameFilter != "none") {
                        val originalPost = posts.getOrNull(position)
                        if (originalPost != null) {
                            val newPosition = filteredPosts.indexOf(originalPost)
                            if (newPosition >= 0) {
                                recyclerView.scrollToPosition(newPosition)
                            }
                        }
                    } else {
                        recyclerView.scrollToPosition(it)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        reactions = intent.getParcelableArrayListExtra<Reaction>("reactions") ?: ArrayList()
        usernameFilter = intent.getStringExtra("filterUsername") ?: "none"
        filteredPosts = ArrayList(posts.filter { it.username == usernameFilter })

        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv

        if (usernameFilter == "none") {
            recyclerView.adapter = FeedAdapter(posts, reactions, this)
        } else {
            if (filteredPosts.isNotEmpty()) {
                recyclerView.adapter = FeedAdapter(filteredPosts, reactions, this)
            } else {
                binding.noPostsTextView.visibility = View.VISIBLE
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        helper.attachToRecyclerView(recyclerView)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val toastShown = sharedPreferences.getBoolean(TOAST_SHOWN_KEY, false)
        val position = intent.getIntExtra("position", -1)

        if (position >= 0) {
            if (usernameFilter != "none") {
                val originalPost = posts.getOrNull(position)
                if (originalPost != null) {
                    val newPosition = filteredPosts.indexOf(originalPost)
                    if (newPosition >= 0) {
                        recyclerView.scrollToPosition(newPosition)
                    }
                }
            } else {
                recyclerView.scrollToPosition(position)
            }
        }

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
            val newIntent = Intent(this, GalleryActivity::class.java).apply {
                putParcelableArrayListExtra("data", posts)
                putParcelableArrayListExtra("reactions", reactions)
                putExtra("galleryFilter", usernameFilter)
            }
            newIntentActivity.launch(newIntent)
        }

        binding.feedProfileBtn.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.feedMessageBtn.setOnClickListener {
            val newIntent = Intent(this, ReactionActivity::class.java).apply {
                putParcelableArrayListExtra("data", posts)
                putParcelableArrayListExtra("reactions", reactions)
            }
            newIntentActivity.launch(newIntent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent().apply {
                putParcelableArrayListExtra("updated_posts", posts)
                putParcelableArrayListExtra("updated_reactions", reactions)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.feedFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            intent.putExtra("data", posts)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        returnUpdatedData()
        super.onBackPressed()
    }


    private fun returnUpdatedData() {
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra("updated_posts", posts)
            putParcelableArrayListExtra("updated_reactions", reactions)
        }
        setResult(RESULT_OK, resultIntent)
    }

    override fun onPostDeleted(position: Int, postId: Int) {
        val deletedPostIndex = posts.indexOfFirst { it.postImageId == postId }

        if (deletedPostIndex >= 0) {
            posts.removeAt(deletedPostIndex)

            posts.forEachIndexed { index, post ->
                if (index >= deletedPostIndex) {
                    post.position -= 1
                }
            }
        }

        filteredPosts = ArrayList(posts.filter { it.username == usernameFilter })
        recyclerView.adapter?.notifyDataSetChanged()

        if (filteredPosts.isEmpty()) {
            binding.noPostsTextView.visibility = View.VISIBLE
        }
    }
}
