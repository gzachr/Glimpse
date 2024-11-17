package com.mobdeve.s12.group8.glimpse

import androidx.compose.runtime.snapshots.Snapshot
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.mobdeve.s12.group8.glimpse.model.User

class FirestoreReferences {
    companion object{
        private var db: FirebaseFirestore? = null
        private var storage: StorageReference? = null
        private var usersRef: CollectionReference? = null

        const val USERS_COLLECTION = "users"

        const val USERNAME_FIELD = "username"

        fun getFirestoreInstance() : FirebaseFirestore{
            if(db == null){
                db = FirebaseFirestore.getInstance()
            }
            return db as FirebaseFirestore
        }

        fun getUserCollectionReference() : CollectionReference{
            if(usersRef == null){
                usersRef = getFirestoreInstance().collection(USERS_COLLECTION)
            }
            return usersRef as CollectionReference
        }

        fun addUser(user: User): Task<DocumentReference> {
            return getUserCollectionReference().add(user)
        }

        fun getUserbyUsername(username: String): Task<QuerySnapshot> {
            return getUserCollectionReference().whereEqualTo(USERNAME_FIELD, username).get()
        }
    }
}