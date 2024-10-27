package com.mobdeve.s12.group8.glimpse

import Post
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFeedBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class FeedActivity : AppCompatActivity() {
    private var posts: ArrayList<Post> = ArrayList()
    private var reactions: ArrayList<Reaction> = ArrayList()
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
                    recyclerView.scrollToPosition(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        reactions = intent.getParcelableArrayListExtra<Reaction>("reactions") ?: ArrayList()

        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv
        recyclerView.adapter = FeedAdapter(posts, reactions)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        helper.attachToRecyclerView(recyclerView)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val toastShown = sharedPreferences.getBoolean(TOAST_SHOWN_KEY, false)
        val position = intent.getIntExtra("position", -1)

        if (position >= 0) {
            recyclerView.scrollToPosition(position)
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
            }
            newIntentActivity.launch(newIntent)
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

    fun updatePosts(updatedPosts: ArrayList<Post>, updatedReactions: ArrayList<Reaction>) {
        posts.clear()
        posts.addAll(updatedPosts)

        reactions.clear()
        reactions.addAll(updatedReactions)
    }
}
