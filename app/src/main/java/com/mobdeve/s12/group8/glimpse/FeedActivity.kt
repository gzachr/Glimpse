package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFeedBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class FeedActivity: AppCompatActivity() {
    private var data: ArrayList<Post> = DataHelper.loadPostData()

    private lateinit var binding: ActivityFeedBinding
    private lateinit var recyclerView: RecyclerView
    private var helper = LinearSnapHelper()
    private var isLiked = false
    private val newIntentActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data

            if (data?.getIntExtra("from", 0)?.equals(1) == true) {
                val position = data.getIntExtra("position", -1)
                position.let {
                    if (position >= 0) {
                        recyclerView.scrollToPosition(position)
                    }
                }
            }
            else if (data?.getIntExtra("from", 0)?.equals(2) == true) {
                val postImageId = data.getIntExtra("postImageId", -1)
                if (postImageId != -1) {
                    val position = this.data.indexOfFirst { it.postImageId == postImageId }
                    if (position >= 0) {
                        recyclerView.scrollToPosition(position)
                    }
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv
        recyclerView.adapter = FeedAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        helper.attachToRecyclerView(recyclerView)

        binding.heartBtn.setOnClickListener {
            if(isLiked)
                binding.heartBtn.setImageResource(R.drawable.heart_icon_outline)
            else
                binding.heartBtn.setImageResource(R.drawable.heart_icon_filled)

            isLiked = !isLiked
        }

        binding.feedViewAllBtn.setOnClickListener{
            val newIntent = Intent(this,GalleryActivity::class.java)
            newIntentActivity.launch(newIntent)
        }

        binding.feedMessageBtn.setOnClickListener {
            val newIntent = Intent(this,ReactionActivity::class.java)
            newIntentActivity.launch(newIntent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            finish()
        }


    }
}