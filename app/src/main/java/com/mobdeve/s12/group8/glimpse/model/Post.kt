package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    var imgUri: String,
    var location: String, //subject to change datatype
    var user: DocumentReference,
    @ServerTimestamp val createdAt: Date?,
    var caption: String
) {
}