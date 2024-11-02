package com.mobdeve.s12.group8.glimpse

import Post
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityFriendsListBinding
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class FriendsListActivity : AppCompatActivity() {
    private var posts: ArrayList<Post> = ArrayList()
    private var friends: ArrayList<Post> = ArrayList()
    private lateinit var binding: ActivityFriendsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        friends = ArrayList(posts.distinctBy { it.username })

        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.friendsRecyclerView.adapter = FriendsListAdapter(friends)

        binding.friendsExitButton.setOnClickListener {
            finish()
        }
    }
}