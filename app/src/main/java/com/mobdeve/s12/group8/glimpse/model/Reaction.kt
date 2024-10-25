package com.mobdeve.s12.group8.glimpse.model

class Reaction(postImageId: Int, userImageId: Int, username: String, reactiontext: String){
    var postImageId = postImageId
        private set

    var userImageId = userImageId
        private set

    var username = username
        private set
    var reactiontext = reactiontext
        private set
}

