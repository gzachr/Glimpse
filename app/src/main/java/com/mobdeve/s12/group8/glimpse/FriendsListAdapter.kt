package com.mobdeve.s12.group8.glimpse

import Post
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.ItemFriendsBinding

class FriendsListAdapter(
    private val postList: ArrayList<Post>,
    private val listener: OnFriendClickListener
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    interface OnFriendClickListener {
        fun onFriendClick(username: String)
    }

    inner class FriendsViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: Post) {
            if (post.username == "user1" || post.username == "none") {
                binding.removeFriend.visibility = View.GONE
            }


            Glide.with(binding.friendImage.context)
                .load(post.userImageId)
                .apply(RequestOptions().transform(RoundedCorners(100)))
                .into(binding.friendImage)

            binding.friendUsername.text = if (post.username == "user1") "You" else post.username
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

        holder.itemView.setOnClickListener {
            val username = post.username
            listener.onFriendClick(username)
        }
    }


}