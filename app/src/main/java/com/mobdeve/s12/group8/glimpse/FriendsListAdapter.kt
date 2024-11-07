package com.mobdeve.s12.group8.glimpse

import OldPost
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.ItemFriendsBinding

class FriendsListAdapter(
    private val oldPostList: ArrayList<OldPost>,
    private val listener: OnFriendClickListener
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    interface OnFriendClickListener {
        fun onFriendClick(username: String)
    }

    inner class FriendsViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(oldPost: OldPost) {
            if (oldPost.username == "user1" || oldPost.username == "none") {
                binding.removeFriend.visibility = View.GONE
            }


            Glide.with(binding.friendImage.context)
                .load(oldPost.userImageId)
                .apply(RequestOptions().transform(RoundedCorners(100)))
                .into(binding.friendImage)

            binding.friendUsername.text = if (oldPost.username == "user1") "You" else oldPost.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return oldPostList.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val post = oldPostList[position]
        holder.bindData(post)

        holder.itemView.setOnClickListener {
            val username = post.username
            listener.onFriendClick(username)
        }
    }


}