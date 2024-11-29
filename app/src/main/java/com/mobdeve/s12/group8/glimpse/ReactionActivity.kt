package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReactionActivity: AppCompatActivity(), ReactionAdapter.OnNotificationsClickListener {
    private lateinit var binding: ActivityReactionsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var currUser: User
    private lateinit var currUserUID: String
    private lateinit var reactions: List<Reaction>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReactionAdapter

    data class PostWithId(
        val postId: String,
        val post: Post
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.reactionRecyclerView

        auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            auth.currentUser?.let { firebaseUser ->
                val query = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
                query.addOnSuccessListener { querySnapshot ->
                    val userDocument = querySnapshot.documents.firstOrNull()
                    userDocument?.let {
                        currUser = it.toObject(User::class.java)!!
                        currUserUID = it.id
                        fetchReactions()
                    }
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        binding.reactionExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onNotificationsClick(postID: String) {
        val postIntent = Intent(this, FeedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(IntentKeys.POST_ID.toString(), postID)
        }
        setResult(RESULT_OK, postIntent)
        startActivity(postIntent)
        finish()
    }

    private fun fetchReactions() {
        FirestoreReferences.getReactionCollectionReference()
            .whereIn(FieldPath.documentId(), currUser.reactionsReceived)
            .orderBy("timeReacted", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { reactionSnapshot ->
                reactions = reactionSnapshot.toObjects(Reaction::class.java)
                val postIDList = reactions.map { it.postId }.distinct()

                FirestoreReferences.getPostCollectionReference()
                    .whereIn(FieldPath.documentId(), postIDList)
                    .get()
                    .addOnSuccessListener { postSnapshot ->
                        val postIdToImgUriMap = postSnapshot.documents.associate { it.id to it.toObject(Post::class.java)?.imgUri }
                        val postReactionsMap = reactions.associate { it.postId to postIdToImgUriMap[it.postId]!! }

                        if (reactions.isEmpty()) {
                            binding.noNotificationsYet.visibility = View.VISIBLE
                        }

                        adapter = ReactionAdapter(reactions, postReactionsMap, this)
                        recyclerView.adapter = adapter
                    }
            }
    }
}
