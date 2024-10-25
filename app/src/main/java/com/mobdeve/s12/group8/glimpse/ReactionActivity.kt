package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.group8.glimpse.databinding.ActivityReactionsBinding
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionActivity: AppCompatActivity() {
    private var data2: ArrayList<Reaction> = DataHelper.loadReactionData()
    private lateinit var binding: ActivityReactionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reactionsExitBtn.setOnClickListener{
            finish()
        }
    }
}
