package com.mobdeve.s12.group8.glimpse

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ItemReactionBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionAdapter(
    private val data: ArrayList<Reaction>,
    private val listener: OnNotificationsClickListener
) : RecyclerView.Adapter<ReactionViewHolder>() {

    interface OnNotificationsClickListener {
        fun onNotificationsClick(postImageId: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val binding = ItemReactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        val currItem = data[position]
        Log.d("ReactionAdapter", "Current item: $currItem")
        holder.bindData(currItem)

        holder.itemView.setOnClickListener {
            listener.onNotificationsClick(currItem.postImageId)
        }
    }
}
