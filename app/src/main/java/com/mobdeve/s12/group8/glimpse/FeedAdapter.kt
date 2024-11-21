package com.mobdeve.s12.group8.glimpse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.mobdeve.s12.group8.glimpse.databinding.FeedLayoutBinding
import OldPost
import android.util.Log
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
import kotlinx.coroutines.withContext

class FeedAdapter(
    options: FirestoreRecyclerOptions<Post>
) : FirestoreRecyclerAdapter<Post, FeedViewHolder>(options) {
    private lateinit var auth: FirebaseAuth
    private var currUserUID: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val feedBinding = FeedLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(feedBinding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int, model: Post) {
        val documentId = snapshots.getSnapshot(position).id
        CoroutineScope(Dispatchers.IO).launch {
            val user = FirestoreReferences.getUserByID(model.userId).await().toObject(User::class.java)

            // get current user details to get UID
            auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { firebaseUser ->
                val userDocument = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
                    .await()
                    .documents
                    .firstOrNull()
                currUserUID = userDocument?.id
            }

            // get reactions list of current user
            val reactionsList = ArrayList<Reaction>()
            val reactionsQuery = FirestoreReferences.getReactionCollectionReference()
                .whereEqualTo("reactorId", currUserUID)
            val reactionsSnapshot = reactionsQuery.get().await()
            reactionsSnapshot.documents.forEach { document ->
                val reaction = document.toObject(Reaction::class.java)
                reactionsList.add(reaction!!)
            }

            withContext(Dispatchers.Main){
                holder.bind(documentId, model, user!!, currUserUID!!, reactionsList)
                holder.setDeleteButtonListener(documentId)
                holder.setReactButtonListener(documentId, currUserUID!!, model.userId)
            }
        }
    }
}