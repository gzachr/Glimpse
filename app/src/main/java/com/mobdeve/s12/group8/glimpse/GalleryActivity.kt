package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import Post
import com.mobdeve.s12.group8.glimpse.model.Reaction

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var binding: ActivityGalleryBinding
    private var posts: ArrayList<Post> = ArrayList()
    private var reactions: ArrayList<Reaction> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        reactions = intent.getParcelableArrayListExtra<Reaction>("reactions") ?: ArrayList()

        binding.recyclerViewPosts.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewPosts.adapter = GalleryAdapter(posts, this)

        binding.galleryMessageBtn.setOnClickListener {
            val newIntent = Intent(this, ReactionActivity::class.java).apply {
                putParcelableArrayListExtra("data", posts)
                putParcelableArrayListExtra("reactions", reactions)
            }
            startActivity(newIntent)
        }

        binding.galleryReturnToHomeBtn.setOnClickListener {
            val resultIntent = Intent(applicationContext, HomeActivity::class.java).apply {
                putParcelableArrayListExtra("updated_posts", posts)
                putParcelableArrayListExtra("updated_reactions", reactions)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            setResult(RESULT_OK, resultIntent)
            startActivity(resultIntent)
            finish()
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