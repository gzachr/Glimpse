package com.mobdeve.s12.group8.glimpse

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionViewHolder(private val binding: ItemReactionBinding): ViewHolder(binding.root) {
    fun bindData(reaction: Reaction) {
        binding.postImage.setImageResource(reaction.postImageId)
        binding.reactionString.text = "${reaction.username} has liked your post."
        binding.reactionDate.text = "${reaction.reactionDate} hours ago"
    }
}