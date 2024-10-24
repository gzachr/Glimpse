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
    private var data: ArrayList<Post> = arrayListOf(
        Post(R.drawable.post1, R.drawable.user1, "user1", "16h ago", "hello world 1"),
        Post(R.drawable.post2, R.drawable.user2, "user2", "15h ago", "hello world 2"),
        Post(R.drawable.post3, R.drawable.user3, "user3", "14h ago", "hello world 3"),
    )
    private lateinit var binding: ActivityFeedBinding
    private lateinit var recyclerView: RecyclerView
    private var helper = LinearSnapHelper()
    private var isLiked = false
    private lateinit var tempButton: ImageButton

    private val newIntentActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            print("hi")
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
        tempButton = binding.feedViewAllBtn

        binding.heartBtn.setOnClickListener {
            if(isLiked)
                binding.heartBtn.setImageResource(R.drawable.heart_icon_outline)
            else
                binding.heartBtn.setImageResource(R.drawable.heart_icon_filled)

            isLiked = !isLiked
        }

        tempButton.setOnClickListener {
            val newIntent = Intent(this,GalleryActivity::class.java)
            newIntentActivity.launch(newIntent)
        }

        binding.feedReturnToHomeBtn.setOnClickListener {
            //TODO return to home activity, remove this activity from stack?
        }

    }
}