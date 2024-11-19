package com.mobdeve.s12.group8.glimpse

import OldPost
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
import com.mobdeve.s12.group8.glimpse.model.OldReaction

class FeedActivity : AppCompatActivity(), FeedAdapter.PostDeleteCallback {
    private var oldPosts: ArrayList<OldPost> = ArrayList()
    private var filteredOldPosts: ArrayList<OldPost> = ArrayList()
    private var oldReactions: ArrayList<OldReaction> = ArrayList()
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
                        val originalPost = oldPosts.getOrNull(position)
                        if (originalPost != null) {
                            val newPosition = filteredOldPosts.indexOf(originalPost)
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
        oldPosts = intent.getParcelableArrayListExtra<OldPost>("data") ?: ArrayList()
        oldReactions = intent.getParcelableArrayListExtra<OldReaction>("reactions") ?: ArrayList()
        usernameFilter = intent.getStringExtra("filterUsername") ?: "none"
        filteredOldPosts = ArrayList(oldPosts.filter { it.username == usernameFilter })

        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv

        if (usernameFilter == "none") {
            recyclerView.adapter = FeedAdapter(oldPosts, oldReactions, this)
        } else {
            if (filteredOldPosts.isNotEmpty()) {
                recyclerView.adapter = FeedAdapter(filteredOldPosts, oldReactions, this)
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
                val originalPost = oldPosts.getOrNull(position)
                if (originalPost != null) {
                    val newPosition = filteredOldPosts.indexOf(originalPost)
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
                putParcelableArrayListExtra("data", oldPosts)
                putParcelableArrayListExtra("reactions", oldReactions)
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
                putParcelableArrayListExtra("data", oldPosts)
                putParcelableArrayListExtra("reactions", oldReactions)
            }
            newIntentActivity.launch(newIntent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent().apply {
                putParcelableArrayListExtra("updated_posts", oldPosts)
                putParcelableArrayListExtra("updated_reactions", oldReactions)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.feedFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            intent.putExtra("data", oldPosts)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        returnUpdatedData()
        super.onBackPressed()
    }


    private fun returnUpdatedData() {
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra("updated_posts", oldPosts)
            putParcelableArrayListExtra("updated_reactions", oldReactions)
        }
        setResult(RESULT_OK, resultIntent)
    }

    override fun onPostDeleted(position: Int, postId: Int) {
        val deletedPostIndex = oldPosts.indexOfFirst { it.postImageId == postId }

        if (deletedPostIndex >= 0) {
            oldPosts.removeAt(deletedPostIndex)

            oldPosts.forEachIndexed { index, post ->
                if (index >= deletedPostIndex) {
                    post.position -= 1
                }
            }
        }

        filteredOldPosts = ArrayList(oldPosts.filter { it.username == usernameFilter })
        recyclerView.adapter?.notifyDataSetChanged()

        if (filteredOldPosts.isEmpty()) {
            binding.noPostsTextView.visibility = View.VISIBLE
        }
    }
}
