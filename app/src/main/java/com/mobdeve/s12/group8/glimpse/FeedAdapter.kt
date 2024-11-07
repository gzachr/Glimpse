package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import OldPost
import com.mobdeve.s12.group8.glimpse.model.OldReaction

class FeedAdapter(private val data: ArrayList<OldPost>, private val oldReactions: ArrayList<OldReaction>, private val postDeleteCallback: PostDeleteCallback): Adapter<FeedViewHolder>() {

    interface PostDeleteCallback {
        fun onPostDeleted(position: Int, postId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val feedBinding = FeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(feedBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(data[position])

        holder.setDeleteButtonListener {
            val removedPost = data[position]
            val removedPostId = removedPost.postImageId

            data.removeAt(position)
            oldReactions.removeAll { reaction -> reaction.postImageId == removedPostId }

            oldReactions.forEach { reaction ->
                reaction.position -= 1
            }

            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
            postDeleteCallback.onPostDeleted(position, removedPostId)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}