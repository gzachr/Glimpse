package com.mobdeve.s12.group8.glimpse.model

class Post(postImageId: Int, userImageId: Int, username: String, createdAt: String, caption: String){
    var postImageId = postImageId
        private set

    var userImageId = userImageId
        private set

    var username = username
        private set

    var createdAt = createdAt
        private set

    var caption = caption
        private set
}