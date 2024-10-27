package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionActivity: AppCompatActivity(), ReactionAdapter.OnNotificationsClickListener {
    private var data: ArrayList<Reaction> = DataHelper.loadReactionData()
    private lateinit var binding: ActivityReactionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.reactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.reactionRecyclerView.adapter = ReactionAdapter(data, this)

        binding.reactionExitButton.setOnClickListener {
            finish()
        }
    }

    override fun onNotificationsClick(position: Int) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(homeIntent)

        val feedIntent = Intent(this, FeedActivity::class.java).apply {
            putExtra("position", position)
        }
        startActivity(feedIntent)

        finish()
    }

}
