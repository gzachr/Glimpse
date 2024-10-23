package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import com.mobdeve.s12.group8.glimpse.model.Post

class FeedAdapter(private val data: ArrayList<Post>): Adapter<FeedViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val feedBinding = FeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(feedBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}