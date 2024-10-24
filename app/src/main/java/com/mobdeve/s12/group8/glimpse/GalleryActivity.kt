package com.mobdeve.s12.group8.glimpse

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GalleryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var postsList: PostsList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        recyclerView = findViewById(R.id.recyclerViewPosts)

        postsList = PostsList()

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        Log.d("GalleryActivity", "Post list size: ${postsList.postList.size}")
        recyclerView.adapter = GalleryAdapter(postsList.postList)
    }
}