package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import OldPost
import android.view.View
import com.mobdeve.s12.group8.glimpse.model.OldReaction

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var binding: ActivityGalleryBinding
    private var oldPosts: ArrayList<OldPost> = ArrayList()
    private var filteredOldPosts: ArrayList<OldPost> = ArrayList()
    private var oldReactions: ArrayList<OldReaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)



        oldPosts = intent.getParcelableArrayListExtra<OldPost>("data") ?: ArrayList()
        oldReactions = intent.getParcelableArrayListExtra<OldReaction>("reactions") ?: ArrayList()
        val usernameFilter = intent.getStringExtra("galleryFilter") ?: "none"
        filteredOldPosts = ArrayList(oldPosts.filter { it.username == usernameFilter })

        binding.recyclerViewPosts.layoutManager = GridLayoutManager(this, 3)

        if (usernameFilter == "none") {
            binding.recyclerViewPosts.adapter = GalleryAdapter(oldPosts, this)
        } else {
            if (filteredOldPosts.isNotEmpty()) {
                binding.recyclerViewPosts.adapter = GalleryAdapter(filteredOldPosts, this)
            } else {
                binding.noPostsGalleryTextView.visibility = View.VISIBLE
            }
        }
        binding.reactionExitButton.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.galleryMessageBtn.setOnClickListener {
            val newIntent = Intent(this, ReactionActivity::class.java).apply {
                putParcelableArrayListExtra("data", oldPosts)
                putParcelableArrayListExtra("reactions", oldReactions)
            }
            startActivity(newIntent)
        }

        binding.galleryReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, HomeActivity::class.java).apply {
                putParcelableArrayListExtra("updated_posts", oldPosts)
                putParcelableArrayListExtra("updated_reactions", oldReactions)
                putExtra("fromGallery", 1)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            setResult(RESULT_OK, resultIntent)
            startActivity(resultIntent)
            finish()
        }

        binding.galleryFriendsBtn.setOnClickListener {
            val intent = Intent(applicationContext, FriendsListActivity::class.java)
            intent.putExtra("data", oldPosts)
            startActivity(intent)
        }
    }

    override fun onPostClick(position: Int) {
        val newIntent = Intent().apply {
            putExtra("position", position)
        }
        setResult(RESULT_OK, newIntent)
        finish()
    }
}