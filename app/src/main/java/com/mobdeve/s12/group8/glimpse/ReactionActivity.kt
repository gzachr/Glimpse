package com.mobdeve.s12.group8.glimpse

import OldPost
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.OldReaction

class ReactionActivity: AppCompatActivity(), ReactionAdapter.OnNotificationsClickListener {
    private var oldPosts: ArrayList<OldPost> = ArrayList()
    private var oldReactions: ArrayList<OldReaction> = ArrayList()
    private lateinit var binding: ActivityReactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        oldPosts = intent.getParcelableArrayListExtra<OldPost>("data") ?: ArrayList()
        oldReactions = intent.getParcelableArrayListExtra<OldReaction>("reactions") ?: ArrayList()

        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.reactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reactionRecyclerView.adapter = ReactionAdapter(oldReactions, this)

        binding.reactionExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onNotificationsClick(position: Int) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putParcelableArrayListExtra("updated_posts", oldPosts)
            putParcelableArrayListExtra("updated_reactions", oldReactions)
            putExtra("open_feed_at_position", position)
            putExtra("usernameFilter", "none")
        }
        startActivity(homeIntent)
        finish()
    }


}
