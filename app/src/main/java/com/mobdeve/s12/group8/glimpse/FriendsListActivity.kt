package com.mobdeve.s12.group8.glimpse

import Post
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFriendsListBinding

class FriendsListActivity : AppCompatActivity(), FriendsListAdapter.OnFriendClickListener {
    private var posts: ArrayList<Post> = ArrayList()
    private var friends: ArrayList<Post> = ArrayList()
    private lateinit var binding: ActivityFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        friends = ArrayList(posts.distinctBy { it.username })

        val userPost = friends.find { it.username == "user1" }
        if (userPost != null) {
            friends.remove(userPost)
            friends.add(userPost)
        } else {
            val user1 = Post(
                postImageId = -1,
                userImageId = R.drawable.user1,
                username = "user1",
                createdAt = "",
                caption = "",
                position = -1
            )
            friends.add(user1)
        }

        val everyonePost = Post(
            postImageId = -1,
            userImageId = R.drawable.friends_icon,
            username = "none",
            createdAt = "",
            caption = "",
            position = -1
        )
        friends.add(0, everyonePost)

        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsRecyclerView.adapter = FriendsListAdapter(friends, this)

        binding.friendsExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onFriendClick(username: String) {
        val filterIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("usernameFilter", username)

        }
        startActivity(filterIntent)
        finish()
    }
}