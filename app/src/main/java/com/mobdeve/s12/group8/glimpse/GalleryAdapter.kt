package com.mobdeve.s12.group8.glimpse

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityGalleryBinding
import com.mobdeve.s12.group8.glimpse.databinding.ItemPostBinding
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import Post
import com.mobdeve.s12.group8.glimpse.model.Reaction

class GalleryAdapter(
    private val postList: ArrayList<Post>,
    private val listener: OnPostClickListener
) : RecyclerView.Adapter<GalleryAdapter.PostViewHolder>() {

    interface OnPostClickListener {
        fun onPostClick(position: Int)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: Post) {
            binding.imageViewPost.setImageResource(post.postImageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]
        holder.bindData(post)

        holder.itemView.setOnClickListener {
            listener.onPostClick(post.position)
        }
    }

}