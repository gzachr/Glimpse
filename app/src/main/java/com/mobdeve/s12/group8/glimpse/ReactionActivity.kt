package com.mobdeve.s12.group8.glimpse

import Post
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionActivity: AppCompatActivity(), ReactionAdapter.OnNotificationsClickListener {
    private var posts: ArrayList<Post> = ArrayList()
    private var reactions: ArrayList<Reaction> = ArrayList()
    private lateinit var binding: ActivityReactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        posts = intent.getParcelableArrayListExtra<Post>("data") ?: ArrayList()
        reactions = intent.getParcelableArrayListExtra<Reaction>("reactions") ?: ArrayList()

        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.reactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reactionRecyclerView.adapter = ReactionAdapter(reactions, this)

        binding.reactionExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onNotificationsClick(position: Int) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putParcelableArrayListExtra("updated_posts", posts)
            putParcelableArrayListExtra("updated_reactions", reactions)
            putExtra("open_feed_at_position", position)
            putExtra("usernameFilter", "none")
        }
        startActivity(homeIntent)
        finish()
    }


}
