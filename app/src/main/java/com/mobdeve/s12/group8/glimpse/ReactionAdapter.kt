package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionAdapter(private val data: ArrayList<Reaction>): Adapter<ReactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_reaction, parent, false)
        return ReactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.bindData(data.get(position))
    }
}