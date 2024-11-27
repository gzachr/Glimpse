package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFriendRequestBinding
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFriendsListBinding
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendRequestActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var currUser: User
    private lateinit var currUserUID: String
    private lateinit var requestQuery: Query
    private lateinit var binding: ActivityFriendRequestBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendRequestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.friendReqRecyclerView

        auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            fetchFriendRequests()
        }

        binding.friendRequestExitBtn.setOnClickListener {
            val intent = Intent(this, FriendsListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun fetchFriendRequests() {
        auth.currentUser?.let { firebaseUser ->
            val query = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
            query.addOnSuccessListener { querySnapshot ->
                val userDocument = querySnapshot.documents.firstOrNull()
                userDocument?.let {
                    currUser = it.toObject(User::class.java)!!
                    currUserUID = it.id

                    if (currUser.friendRequestList.isNotEmpty()) {
                        binding.friendReqRecyclerView.visibility = View.VISIBLE
                        requestQuery = FirestoreReferences.getUserCollectionReference()
                            .whereIn(FieldPath.documentId(), currUser.friendRequestList)

                        val requests = FirestoreRecyclerOptions.Builder<User>()
                            .setQuery(requestQuery, User::class.java)
                            .build()

                        recyclerView.layoutManager = LinearLayoutManager(this)
                        adapter = FriendRequestAdapter(requests, currUserUID, binding.noFriendRequestYet)
                        recyclerView.adapter = adapter
                        adapter.startListening()
                    } else {
                        binding.friendReqRecyclerView.visibility = View.GONE
                        binding.noFriendRequestYet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}