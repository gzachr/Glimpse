package com.mobdeve.s12.group8.glimpse

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.mobdeve.s12.group8.glimpse.databinding.ItemRequestBinding
import com.mobdeve.s12.group8.glimpse.model.User

class FriendRequestAdapter(
    requests: FirestoreRecyclerOptions<User>,
    private val currUserUID: String,
    private val noFriendRequestYet: View
) : FirestoreRecyclerAdapter<User, FriendRequestAdapter.FriendRequestViewHolder>(requests) {

    inner class FriendRequestViewHolder(private val binding: ItemRequestBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, userID: String) {
            Glide.with(binding.requestImage.context)
                .load(user.profileImage)
                .apply(RequestOptions().transform(RoundedCorners(50)))
                .into(binding.requestImage)

            binding.requestUsername.text = user.username

            binding.acceptRequest.setOnClickListener {
                FirestoreReferences.getUserByID(currUserUID)
                    .addOnSuccessListener { userDocSnapshot ->
                        userDocSnapshot.reference.update(
                            "friendRequestList", FieldValue.arrayRemove(userID)
                        )
                        userDocSnapshot.reference.update(
                            "friendList", FieldValue.arrayUnion(userID)
                        )
                        (binding.root.context as FriendRequestActivity).fetchFriendRequests()
                    }
            }

            binding.declineRequest.setOnClickListener {
                FirestoreReferences.getUserByID(currUserUID)
                    .addOnSuccessListener { userDocSnapshot ->
                        userDocSnapshot.reference.update(
                            "friendRequestList", FieldValue.arrayRemove(userID)
                        )
                        (binding.root.context as FriendRequestActivity).fetchFriendRequests()
                    }
            }
        }
    }
    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int, model: User) {
        val documentId = snapshots.getSnapshot(position).id
        holder.bind(model, documentId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val feedBinding = ItemRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(feedBinding)
    }

    override fun onDataChanged() {
        if (itemCount == 0) {
            noFriendRequestYet.visibility = View.VISIBLE
        } else {
            noFriendRequestYet.visibility = View.GONE
        }
    }
}
