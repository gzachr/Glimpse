package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.DocumentReference

data class User(
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var profileImage: String? = null, // link to photo in Firebase Storage
    var friendList: List<String?> = emptyList(),  // List of  userId
    var reactionsReceived: List<String> = emptyList(),
    var postsLiked: List<String> = emptyList(),
    var friendRequestList: List<String> = emptyList()
) {
}

