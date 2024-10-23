package com.mobdeve.s12.group8.glimpse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFeedBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class FeedActivity: AppCompatActivity() {
    private var data: ArrayList<Post> = arrayListOf(
        Post(R.drawable.post1, R.drawable.user1, "user1"),
        Post(R.drawable.post2, R.drawable.user2, "user2"),
        Post(R.drawable.post3, R.drawable.user3, "user3"),
    )
    private lateinit var binding: ActivityFeedBinding
    private lateinit var recyclerView: RecyclerView
    private var helper = LinearSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.feedRv
        recyclerView.adapter = FeedAdapter(data)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        helper.attachToRecyclerView(recyclerView)
    }
}