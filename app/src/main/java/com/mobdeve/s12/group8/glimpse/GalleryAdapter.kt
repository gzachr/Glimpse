package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ItemPostBinding
import OldPost

class GalleryAdapter(
    private val oldPostList: ArrayList<OldPost>,
    private val listener: OnPostClickListener
) : RecyclerView.Adapter<GalleryAdapter.PostViewHolder>() {

    interface OnPostClickListener {
        fun onPostClick(position: Int)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(oldPost: OldPost) {
            binding.imageViewPost.setImageResource(oldPost.postImageId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return oldPostList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = oldPostList[position]
        holder.bindData(post)

        holder.itemView.setOnClickListener {
            listener.onPostClick(post.position)
        }
    }

}