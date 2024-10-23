package com.mobdeve.s12.group8.glimpse

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class FeedViewHolder(private val binding: FeedLayoutBinding): ViewHolder(binding.root) {
    fun bind(post: Post) {
        Glide.with(binding.feedUserIv.context)
            .load(post.userImageId)
            .apply(RequestOptions().transform(RoundedCorners(50)))
            .into(binding.feedUserIv)

        binding.feedPostIv.post {
            val width = binding.feedPostIv.width
            val layoutParams = binding.feedPostIv.layoutParams
            layoutParams.height = width
            binding.feedPostIv.layoutParams = layoutParams
        }

        // Load post image with rounded corners
        Glide.with(binding.feedPostIv.context)
            .load(post.postImageId)
            .into(binding.feedPostIv)

        binding.feedUsernameTv.text = post.username
    }
}