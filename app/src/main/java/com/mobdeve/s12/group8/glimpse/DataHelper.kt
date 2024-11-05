package com.mobdeve.s12.group8.glimpse

import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import java.util.ArrayList

object DataHelper {
    fun loadPostData(): ArrayList<Post> {
        val data = ArrayList<Post>()
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post1, R.drawable.user1, "user1", "7h ago", "mood be like"
=======
                R.drawable.post1, R.drawable.user1, "user1", "1h ago", "mood be like",0
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post2, R.drawable.user2, "user2", "8h ago", "my two loves"
=======
                R.drawable.post2, R.drawable.user2, "user2", "2h ago", "my two loves", 1
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post3, R.drawable.user3, "user3", "10h ago", "my eye bruh"
=======
                R.drawable.post5, R.drawable.user09, "user09", "3h ago", "mamma mia",2
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post4, R.drawable.user6, "user6", "11h ago", "heat is crazy"
=======
                R.drawable.post4, R.drawable.user6, "user6", "4h ago", "to light",3
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post5, R.drawable.user9, "user9", "13h ago", "mamma mia"
=======
                R.drawable.post11, R.drawable.user6, "user6", "11h ago", "darkness turns",4
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post6, R.drawable.user1, "user1", "14h ago", "errands"
=======
                R.drawable.post03, R.drawable.user1, "user1", "12h ago", "eye bruh", 5
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post7, R.drawable.user4, "user4", "17h ago", "At long last..."
=======
                R.drawable.post6, R.drawable.user1, "user1", "14h ago", "errands",6
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post8, R.drawable.user7, "user7", "19h ago", "shrine core"
=======
                R.drawable.post7, R.drawable.user4, "user4", "17h ago", "At long last...",7
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post9, R.drawable.user1, "user1", "20h ago", "it raining #staysafe"
=======
                R.drawable.post13, R.drawable.user6, "user6", "18h ago", "mamma mia",8
>>>>>>> Stashed changes
            )
        )
        data.add(
            Post(
<<<<<<< Updated upstream
                R.drawable.post10, R.drawable.user8, "user8", "21h ago", "fun times"
=======
                R.drawable.post08, R.drawable.user7, "user7", "19h ago", "sigma grind core",9
            )
        )
        data.add(
            Post(
                R.drawable.post09, R.drawable.user1, "user1", "20h ago", "it raining #staysafe",10
            )
        )
        data.add(
            Post(
                R.drawable.post10, R.drawable.user8, "user8", "21h ago", "fun times",11
            )
        )
        data.add(
            Post(
                R.drawable.post14, R.drawable.user6, "user6", "21h ago", "clean-up necessary",12
            )
        )
        data.add(
            Post(
                R.drawable.post16, R.drawable.user3, "user3", "22h ago", "Catto",13
            )
        )
        data.add(
            Post(
                R.drawable.post12, R.drawable.user2, "user2", "22h ago", "she cheated on me..",14
            )
        )
        data.add(
            Post(
                R.drawable.post15, R.drawable.user10, "user10", "23h ago", "BIBBIEEE",15
            )
        )
        data.add(
            Post(
                R.drawable.post19, R.drawable.user3, "user3", "23h ago", "slep",16
            )
        )
        data.add(
            Post(
                R.drawable.post17, R.drawable.user4, "user4", "1d ago", "mmmmmm...",17
            )
        )
        data.add(
            Post(
                R.drawable.post18, R.drawable.user8, "user8", "1d ago", "blew up",18
>>>>>>> Stashed changes
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