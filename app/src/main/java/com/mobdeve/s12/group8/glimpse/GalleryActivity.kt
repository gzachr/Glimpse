package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var binding: ActivityGalleryBinding
    private var data: ArrayList<Post> = DataHelper.loadPostData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewPosts.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerViewPosts.adapter = GalleryAdapter(data, this)

        binding.reactionExitButton.setOnClickListener {
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.galleryMessageBtn.setOnClickListener{
            val intent = Intent(applicationContext, ReactionActivity::class.java)
            startActivity(intent)
        }

        binding.galleryReturnToHomeBtn.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onPostClick(position: Int) {
        val newIntent = Intent()
        newIntent.putExtra("from", 1)
        newIntent.putExtra("position", position)
        setResult(RESULT_OK, newIntent)
        finish()
    }
}