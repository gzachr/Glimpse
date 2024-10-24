package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryAdapter(private val postList: ArrayList<Post>) : RecyclerView.Adapter<GalleryAdapter.PostViewHolder>() {
    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewPost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val imageResId = postList[position]
        holder.imageView.setImageResource(imageResId.postImageId)

        holder.imageView.setOnClickListener {
            val context = holder.imageView.context
            val intent = Intent(context, FeedActivity::class.java)
            context.startActivity(intent)
        }
    }

}