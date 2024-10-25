package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionActivity: AppCompatActivity() {
    private var data2: ArrayList<Reaction> = DataHelper.loadReactionData()
    private lateinit var binding: ActivityReactionsBinding
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.recyclerViewReactions)

        recyclerView.adapter = ReactionAdapter(data2)
        binding.reactionsExitBtn.setOnClickListener{
            finish()
        }
    }
}
