package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryAdapter(
    private val postList: ArrayList<Post>,
    private val listener: OnPostClickListener
) : RecyclerView.Adapter<GalleryAdapter.PostViewHolder>() {

    interface OnPostClickListener {
        fun onPostClick(position: Int)
    }

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
        val post = postList[position]
        holder.imageView.setImageResource(post.postImageId)

        holder.imageView.setOnClickListener {
            listener.onPostClick(position)
        }
    }

}