package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedAdapter(
    options: FirestoreRecyclerOptions<Post>,
    private val noPostsTextView: View
): FirestoreRecyclerAdapter<Post, FeedViewHolder>(options) {
    private lateinit var auth: FirebaseAuth
    private var currUserUID: String? = null

    init {
        CoroutineScope(Dispatchers.Main).launch {
            auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { firebaseUser ->
                val userDocument = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
                    .await()
                    .documents
                    .firstOrNull()
                currUserUID = userDocument?.id
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val feedBinding = FeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(feedBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, model: Post) {
        val documentId = snapshots.getSnapshot(position).id
        CoroutineScope(Dispatchers.Main).launch {
            val user = FirestoreReferences.getUserByID(model.userId).await().toObject(User::class.java)
            val currUserReactedPostsID: MutableList<String> = mutableListOf()

            val reactionsQuery = FirestoreReferences.getReactionCollectionReference()
                .whereEqualTo("reactorId", currUserUID)
            val reactionsSnapshot = reactionsQuery.get().await()
            reactionsSnapshot.documents.forEach { document ->
                val reaction = document.toObject(Reaction::class.java)
                currUserReactedPostsID.add(reaction!!.postId)
            }

            holder.bind(documentId, model, user!!, currUserUID!!, currUserReactedPostsID)
            holder.setDeleteButtonListener(documentId)
            holder.setReactButtonListener(documentId, currUserUID!!, model.userId)
        }
    }

    override fun onDataChanged() {
        if (itemCount == 0) {
            noPostsTextView.visibility = View.VISIBLE
        } else {
            noPostsTextView.visibility = View.GONE
        }
    }
}