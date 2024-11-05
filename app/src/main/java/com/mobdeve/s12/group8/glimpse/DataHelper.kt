package com.mobdeve.s12.group8.glimpse

import Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import java.util.ArrayList

object DataHelper {
    fun loadPostData(): ArrayList<Post> {
        val data = ArrayList<Post>()
        data.add(
            Post(
                R.drawable.post1, R.drawable.user1, "user1", "7h ago", "mood be like",0
            )
        )
        data.add(
            Post(
                R.drawable.post2, R.drawable.user2, "user2", "8h ago", "my two loves", 1
            )
        )
        data.add(
            Post(
                R.drawable.post5, R.drawable.user09, "user09", "13h ago", "mamma mia",2
            )
        )
        data.add(
            Post(
                R.drawable.post03, R.drawable.user1, "user1", "1h ago", "eye bruh", 3
            )
        )
        data.add(
            Post(
                R.drawable.post6, R.drawable.user1, "user1", "14h ago", "errands",4
            )
        )
        data.add(
            Post(
                R.drawable.post7, R.drawable.user4, "user4", "17h ago", "At long last...",5
            )
        )
        data.add(
            Post(
                R.drawable.post08, R.drawable.user7, "user7", "19h ago", "sigma grind core",6
            )
        )
        data.add(
            Post(
                R.drawable.post09, R.drawable.user1, "user1", "20h ago", "it raining #staysafe",7
            )
        )
        data.add(
            Post(
                R.drawable.post10, R.drawable.user8, "user8", "21h ago", "fun times",8
            )
        )
        data.add(
            Post(
                R.drawable.post12, R.drawable.user2, "user2", "22h ago", "she cheated on me..",9
            )
        )
        data.add(
            Post(
                R.drawable.post4, R.drawable.user6, "user6", "12h ago", "let there be light",10
            )
        )
        data.add(
            Post(
                R.drawable.post15, R.drawable.user09, "user09", "13h ago", "BIBIEEEE",11
            )
        )
        data.add(
            Post(
                R.drawable.post16, R.drawable.user5, "user5", "14h ago", "Catto Catto Catto",12
            )
        )
        data.add(
            Post(
                R.drawable.post17, R.drawable.user4, "user4", "14h ago", "do not kill yourself",13
            )
        )
        data.add(
            Post(
                R.drawable.post11, R.drawable.user6, "user6", "15h ago", "Braving the darkness",14
            )
        )
        data.add(
            Post(
                R.drawable.post18, R.drawable.user10, "user10", "17h ago", "its deep, bros",15
            )
        )
        data.add(
            Post(
                R.drawable.post13, R.drawable.user6, "user6", "18h ago", "Scrub Daddy works",16
            )
        )
        data.add(
            Post(
                R.drawable.post19, R.drawable.user3, "user3", "19h ago", "Slep",17
            )
        )
        data.add(
            Post(
                R.drawable.post14, R.drawable.user6, "user6", "20h ago", "Scrub Daddy Test",18
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
                R.drawable.post09, "user3", "1", 7
            )
        )
        data.add(
            Reaction(
                R.drawable.post09, "user7", "1", 7
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user10", "2", 4
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user8", "3", 4
            )
        )
        data.add(
            Reaction(
                R.drawable.post1, "user4", "3", 0
            )
        )
        data.add(
            Reaction(
                R.drawable.post6, "user5", "3", 4
            )
        )
        data.add(
            Reaction(
                R.drawable.post09, "user8", "4", 7
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