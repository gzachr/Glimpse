package com.mobdeve.s12.group8.glimpse

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
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.mobdeve.s12.group8.glimpse.databinding.ItemFriendsBinding
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendsListAdapter(
    private val friends: MutableList<User>,
    private val currUserUID: String,
    private val currUserUsername: String,
    private val listener: OnFriendClickListener
) : RecyclerView.Adapter<FriendsListAdapter.FriendsViewHolder>() {

    interface OnFriendClickListener {
        fun onFriendClick(userID: String)
    }

    inner class FriendsViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: User) {
            if (user.username == "Everyone" || user.username == currUserUsername) {
                binding.removeFriend.visibility = View.GONE
            }

            Glide.with(binding.friendImage.context)
                .load(user.profileImage)
                .apply(RequestOptions().transform(RoundedCorners(50)))
                .into(binding.friendImage)

            binding.friendUsername.text = if (user.username == currUserUsername) "You" else user.username

            binding.removeFriend.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val res = FirestoreReferences.getUserByEmail(user.email!!).await()
                    val uid = res.documents[0].id
                    FirestoreReferences.getUserByID(currUserUID)
                        .addOnSuccessListener { userDocSnapshot ->
                            userDocSnapshot.reference.update(
                                "friendList", FieldValue.arrayRemove(uid)
                            ).addOnSuccessListener {
                                removeUserFromAdapter(user)
                            }
                        }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        val binding = ItemFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) {
        val user = friends[position]
        holder.bindData(user)
        holder.itemView.setOnClickListener {
            if (user.username != "Everyone") {
                val query = FirestoreReferences.getUserCollectionReference()
                    .whereEqualTo("email", user.email)

                query.get().addOnSuccessListener { querySnapshot ->
                    val document = querySnapshot.documents.first()
                    listener.onFriendClick(document.id)
                }
            } else {
                listener.onFriendClick("Everyone")
            }
        }
    }

    private fun removeUserFromAdapter(user: User) {
        val index = friends.indexOf(user)
        friends.removeAt(index)
        notifyItemRemoved(index)
    }
}