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

        val everyonePost = Post(
            postImageId = -1,  // Use a placeholder ID if applicable
            userImageId = R.drawable.friends_icon,
            username = "none",
            createdAt = "",
            caption = "",
            position = -1  // Use -1 or any other value that signifies a special case
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