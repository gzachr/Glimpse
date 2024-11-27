package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFriendsListBinding
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FriendsListActivity : AppCompatActivity(), FriendsListAdapter.OnFriendClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var everyone: User
    private lateinit var currUser: User
    private lateinit var currUserUID: String
    private lateinit var binding: ActivityFriendsListBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = binding.friendsRecyclerView

        auth = FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            auth.currentUser?.let { firebaseUser ->
                val query = FirestoreReferences.getUserByEmail(firebaseUser.email!!)
                query.addOnSuccessListener { querySnapshot ->
                    val userDocument = querySnapshot.documents.firstOrNull()
                    userDocument?.let {
                        currUser = it.toObject(User::class.java)!!
                        currUserUID = it.id
                        fetchFriends()
                    }
                }
            }
        }

        binding.addNewFriendBt.setOnClickListener {
            val username = binding.addEt.text.toString()

            if (username != "") {
                FirestoreReferences.getUserByUsername(username)
                    .addOnSuccessListener { querySnapshot ->
                        val userDocSnapshot = querySnapshot.documents.firstOrNull()
                        if (userDocSnapshot != null) {
                            userDocSnapshot.get("friendRequestList")?.let { currentRequests ->
                                if ((currentRequests as? List<String>)?.contains(currUserUID) == true) {
                                    Toast.makeText(this, "Friend Request to user was already sent", Toast.LENGTH_LONG).show()
                                } else {
                                    userDocSnapshot.reference.update(
                                        "friendRequestList", FieldValue.arrayUnion(currUserUID)
                                    )
                                    Toast.makeText(this, "Friend Request Sent", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }  else {
                            Toast.makeText(this, "No user exists with that username", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please place a username in the field", Toast.LENGTH_LONG).show()
            }
        }

        binding.friendRequestBtn.setOnClickListener {
            val intent = Intent(this, FriendRequestActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.friendsExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onFriendClick(userID: String) {
        val filterIntent = Intent(applicationContext, FeedActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(IntentKeys.USER_ID_FILTER.toString(), userID)
        }
        startActivity(filterIntent)
        finish()
    }

    private fun fetchFriends() {
        CoroutineScope(Dispatchers.Main).launch {
            everyone = FirestoreReferences.getUserByID("KIAeAe6VPsLWJiYWfq8Y").await().toObject(User::class.java)!!
            if (currUser.friendList.isNotEmpty()) {
                FirestoreReferences.getUserCollectionReference()
                    .whereIn(FieldPath.documentId(), currUser.friendList)
                    .orderBy("username", com.google.firebase.firestore.Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val friendsList = querySnapshot.toObjects(User::class.java).toMutableList()
                        friendsList.sortBy { it.username?.lowercase() }
                        friendsList.add(0, everyone)
                        friendsList.add(currUser)
                        setupAdapter(friendsList)
                    }
            } else {
                val friendsList = mutableListOf<User>()
                friendsList.add(currUser)
                setupAdapter(friendsList)
                binding.noFriendsYet.visibility = View.VISIBLE
            }
        }
    }

    private fun setupAdapter(friendsList: MutableList<User>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FriendsListAdapter(friendsList, currUserUID, currUser.username!!, this, this)
        recyclerView.adapter = adapter
    }

    fun checkEmptyFriendsList(size: Int) {
        binding.noFriendsYet.visibility = if (size == 1) View.VISIBLE else View.GONE
    }
}