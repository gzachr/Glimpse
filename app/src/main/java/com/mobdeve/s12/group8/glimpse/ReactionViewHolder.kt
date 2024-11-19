package com.mobdeve.s12.group8.glimpse

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import com.mobdeve.s12.group8.glimpse.model.OldReaction

class ReactionViewHolder(private val binding: ItemReactionBinding): ViewHolder(binding.root) {
    fun bindData(oldReaction: OldReaction) {
        binding.postImage.setImageResource(oldReaction.postImageId)
        binding.reactionString.text = "${oldReaction.username} has liked your post."
        binding.reactionDate.text = "${oldReaction.reactionDate} hours ago"
    }
}