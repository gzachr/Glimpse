package com.mobdeve.s12.group8.glimpse

import Post
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ItemFriendsBinding

class FriendsListAdapter(
    private val postList: ArrayList<Post>
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    inner class FriendsViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: Post) {
            binding.friendImage.setImageResource(post.userImageId)
            binding.friendUsername.text = "${post.username}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val post = postList[position]
        holder.bindData(post)
    }


}