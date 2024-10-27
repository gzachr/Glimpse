package com.mobdeve.s12.group8.glimpse

import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import java.util.ArrayList

object DataHelper {
    fun loadPostData(): ArrayList<Post> {
        val data = ArrayList<Post>()
        data.add(
            Post(
                R.drawable.post1, R.drawable.user1, "user1", "16h ago", "hello world 1"
            )
        )
        data.add(
            Post(
                R.drawable.post2, R.drawable.user2, "user2", "15h ago", "hello world 2"
            )
        )
        data.add(
            Post(
                R.drawable.post3, R.drawable.user3, "user3", "14h ago", "hello world 3"
            )
        )
        return data
    }

    fun loadReactionData(): ArrayList<Reaction> {
        val data = ArrayList<Reaction>()
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user1", "16"
            )
        )


        return data
    }
}