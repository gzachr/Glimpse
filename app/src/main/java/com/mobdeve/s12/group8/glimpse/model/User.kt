package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.DocumentReference

data class User(
    var username: String? = null,
    var password: Int? = null,
    var profileImage: String? = null, // link to photo in Firebase Storage
    var friendList: List<DocumentReference?> = emptyList(),  // List of references to friends
    var posts: List<DocumentReference?> = emptyList(),
    var reactionsReceived: List<DocumentReference> = emptyList()
)

