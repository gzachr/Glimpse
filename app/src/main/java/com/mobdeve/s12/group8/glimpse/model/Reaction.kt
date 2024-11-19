package com.mobdeve.s12.group8.glimpse.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Reaction (
    var reactor: DocumentReference,
    var post: DocumentReference,
    @ServerTimestamp val date: Date
) {

}
