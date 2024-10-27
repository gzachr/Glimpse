package com.mobdeve.s12.group8.glimpse

import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import java.util.ArrayList

object DataHelper {
    fun loadPostData(): ArrayList<Post> {
        val data = ArrayList<Post>()
        data.add(
            Post(
                R.drawable.post1, R.drawable.user1, "user1", "7h ago", "mood be like"
            )
        )
        data.add(
            Post(
                R.drawable.post2, R.drawable.user2, "user2", "8h ago", "my two loves"
            )
        )
        data.add(
            Post(
                R.drawable.post3, R.drawable.user3, "user3", "10h ago", "my eye bruh"
            )
        )
        data.add(
            Post(
                R.drawable.post4, R.drawable.user6, "user6", "11h ago", "heat is crazy"
            )
        )
        data.add(
            Post(
                R.drawable.post5, R.drawable.user9, "user9", "13h ago", "mamma mia"
            )
        )
        data.add(
            Post(
                R.drawable.post6, R.drawable.user1, "user1", "14h ago", "errands"
            )
        )
        data.add(
            Post(
                R.drawable.post7, R.drawable.user4, "user4", "17h ago", "At long last..."
            )
        )
        data.add(
            Post(
                R.drawable.post8, R.drawable.user7, "user7", "19h ago", "sigma grind core"
            )
        )
        data.add(
            Post(
                R.drawable.post9, R.drawable.user1, "user1", "20h ago", "it raining #staysafe"
            )
        )
        data.add(
            Post(
                R.drawable.post10, R.drawable.user8, "user8", "21h ago", "fun times"
            )
        )
        return data
    }

    fun loadReactionData(): ArrayList<Reaction> {
        val data = ArrayList<Reaction>()
        data.add(
            Reaction(
                R.drawable.post1, "user2", "1", 0
            )
        )
        data.add(
            Reaction(
                R.drawable.post9, "user3", "1", 8
            )
        )
        data.add(
            Reaction(
                R.drawable.post9, "user7", "1", 8
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user10", "2", 5
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user8", "3", 5
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user4", "3", 0
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user5", "3", 5
            )
        )
        data.add(
            Reaction(
                R.drawable.post9, "user8", "4", 8
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user3", "4", 0
            )
        )


        return data
    }
}