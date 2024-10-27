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
        fun onNotificationsClick(position: Int)
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
        holder.bindData(currItem)

        holder.itemView.setOnClickListener {
            val pos = currItem.position
            listener.onNotificationsClick(pos)
        }
    }
}
