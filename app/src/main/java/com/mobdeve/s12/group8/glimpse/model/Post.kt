package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Post(
    var imgUri: String = "",
    var location: GeoPoint = GeoPoint(0.0, 0.0), //subject to change datatype
    var userId: String = "",
    var caption: String = "",
    @ServerTimestamp val createdAt: Date? = null,
) {
}