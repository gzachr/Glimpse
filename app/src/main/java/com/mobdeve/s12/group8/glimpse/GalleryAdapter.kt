package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ItemPostBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s12.group8.glimpse.model.Post

class GalleryAdapter(
    options: FirestoreRecyclerOptions<Post>,
    private val listener: OnPostClickListener
) : FirestoreRecyclerAdapter<Post, GalleryAdapter.PostViewHolder>(options) {

    interface OnPostClickListener {
        fun onPostClick(postID: String)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(post: Post) {
            Glide.with(binding.imageViewPost.context)
                .load(post.imgUri)
                .apply(RequestOptions().transform(RoundedCorners(16)))
                .into(binding.imageViewPost)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.bindData(model)
        val documentId = snapshots.getSnapshot(position).id

        holder.itemView.setOnClickListener {
            listener.onPostClick(documentId)
        }
    }

}