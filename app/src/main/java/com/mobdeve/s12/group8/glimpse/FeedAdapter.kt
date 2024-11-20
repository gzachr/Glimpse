package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import OldPost
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s12.group8.glimpse.model.OldReaction
import com.mobdeve.s12.group8.glimpse.model.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedAdapter(options: FirestoreRecyclerOptions<Post>):
    FirestoreRecyclerAdapter<Post, FeedViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val feedBinding = FeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(feedBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, model: Post) {
        val documentId = snapshots.getSnapshot(position).id
        CoroutineScope(Dispatchers.Main).launch {
            holder.bind(model)
            holder.setDeleteButtonListener(documentId)
        }
    }
}