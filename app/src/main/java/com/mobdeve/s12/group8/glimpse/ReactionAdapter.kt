package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionAdapter(
    private val reactions: List<Reaction>,
    private val postReactionsMap: Map<String, String?>,
    private val listener: OnNotificationsClickListener
) : RecyclerView.Adapter<ReactionViewHolder>() {

    interface OnNotificationsClickListener {
        fun onNotificationsClick(postID: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val binding = ItemReactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reactions.size
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val currItem = reactions[position]
        holder.bindData(currItem, postReactionsMap)

        holder.itemView.setOnClickListener {
            listener.onNotificationsClick(currItem.postId)
        }
    }
}
