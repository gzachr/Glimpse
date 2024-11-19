package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    var imgUri: String,
    var location: GeoPoint, //subject to change datatype
    var user: DocumentReference,
    var caption: String,
    @ServerTimestamp val createdAt: Date? = null,
) {
}