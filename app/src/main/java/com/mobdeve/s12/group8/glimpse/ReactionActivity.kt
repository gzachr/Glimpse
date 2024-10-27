package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
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

    override fun onNotificationsClick(postImageId: Int) {
        val newIntent = Intent(this,FeedActivity::class.java)
        newIntent.putExtra("from", 2)
        newIntent.putExtra("postImageId", postImageId)
        setResult(RESULT_OK, newIntent)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(newIntent)
        finish()
    }
}