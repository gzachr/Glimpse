package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryActivity : AppCompatActivity(), GalleryAdapter.OnPostClickListener {
    private lateinit var recyclerView: RecyclerView
    private var data: ArrayList<Post> = DataHelper.loadPostData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        recyclerView = findViewById(R.id.recyclerViewPosts)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = GalleryAdapter(data, this)
    }

    override fun onPostClick(position: Int) {
        val newIntent = Intent()
        newIntent.putExtra("position", position)
        setResult(RESULT_OK, newIntent)
        finish()
    }
}