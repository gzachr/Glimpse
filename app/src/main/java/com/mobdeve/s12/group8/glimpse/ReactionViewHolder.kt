package com.mobdeve.s12.group8.glimpse

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mobdeve.s12.group8.glimpse.model.Reaction

class ReactionViewHolder(itemView: View): ViewHolder(itemView) {
    private val postImg: ImageView = itemView.findViewById(R.id.postImg)
    private val userImageId: ImageView = itemView.findViewById(R.id.dp)
    private val reactorname: TextView = itemView.findViewById(R.id.reactorname)
    private val reactiontext: TextView = itemView.findViewById(R.id.reactiontext)


    fun bindData(reaction: Reaction) {
        postImg.setImageResource(reaction.postImageId)
        userImageId.setImageResource(reaction.userImageId)
        reactorname.text = reaction.username
        reactiontext.text = reaction.reactiontext
    }
}