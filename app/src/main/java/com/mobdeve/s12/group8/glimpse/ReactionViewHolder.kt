package com.mobdeve.s12.group8.glimpse

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.concurrent.TimeUnit

class ReactionViewHolder(private val binding: ItemReactionBinding): ViewHolder(binding.root) {
    fun bindData(reaction: Reaction, postReactionsMap: Map<String, String?>) {
        val imgUri = postReactionsMap[reaction.postId]

        Glide.with(binding.postImage.context)
            .load(imgUri)
            .apply(RequestOptions().transform(RoundedCorners(8)))
            .into(binding.postImage)

        CoroutineScope(Dispatchers.Main).launch {
            bindUser(reaction.reactorId)
        }

        val timeReacted = formatTimestampToRelative(reaction.timeReacted)
        binding.reactionDate.text = timeReacted
    }

    private suspend fun bindUser(reactorId: String) {
        val documentSnapshot = FirestoreReferences.getUserByID(reactorId).await()
        val userProfile = documentSnapshot.toObject(User::class.java)?.profileImage
        var username = documentSnapshot.toObject(User::class.java)?.username

        if (username?.length!! > 9) {
            username = username.substring(0, 6) + "..."
        }

        binding.reactionString.text = "$username has liked your post."

        Glide.with(binding.userImage.context)
            .load(userProfile)
            .apply(RequestOptions().transform(RoundedCorners(50)))
            .into(binding.userImage)
    }

    private fun formatTimestampToRelative(timestamp: Date?): String {
        if (timestamp == null) return "Unknown"

        val now = System.currentTimeMillis()
        val createdAt = timestamp.time
        val durationMillis = now - createdAt

        val days = TimeUnit.MILLISECONDS.toDays(durationMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(durationMillis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60

        return when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "Just now"
        }
    }
}