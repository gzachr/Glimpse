package com.mobdeve.s12.group8.glimpse

import android.net.Uri
import androidx.compose.runtime.snapshots.Snapshot
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobdeve.s12.group8.glimpse.model.User

class FirestoreReferences {
    companion object{
        private var db: FirebaseFirestore? = null
        private var storage: StorageReference? = null
        private var usersRef: CollectionReference? = null
        private var postsRef: CollectionReference? = null
        private var reactionsRef: CollectionReference? = null

        const val USERS_COLLECTION = "users"
        const val POSTS_COLLECTION = "posts"
        const val REACTIONS_COLLECTION = "reactions"

        const val USERNAME_FIELD = "username"

        fun getFirestoreInstance() : FirebaseFirestore{
            if(db == null)
                db = FirebaseFirestore.getInstance()

            return db as FirebaseFirestore
        }

        fun getStorageInstance(): StorageReference {
            if(storage == null)
                storage = FirebaseStorage.getInstance().reference

            return storage as StorageReference
        }

        fun getUserCollectionReference() : CollectionReference{
            if(usersRef == null)
                usersRef = getFirestoreInstance().collection(USERS_COLLECTION)

            return usersRef as CollectionReference
        }

        fun getPostCollectionReference(): CollectionReference{
            if(postsRef == null)
                    postsRef = getFirestoreInstance().collection(POSTS_COLLECTION)

            return postsRef as CollectionReference
        }

        fun getReactionCollectionReference(): CollectionReference{
            if(reactionsRef == null)
                    reactionsRef = getFirestoreInstance().collection(REACTIONS_COLLECTION)

            return reactionsRef as CollectionReference
        }


        fun addUser(user: User): Task<DocumentReference> {
            return getUserCollectionReference().add(user)
        }

        fun getUserbyUsername(username: String): Task<QuerySnapshot> {
            return getUserCollectionReference().whereEqualTo(USERNAME_FIELD, username).get()
        }

        fun getDefaultUserPhoto(): Task<Uri> {
            val path = "profile_imgs/default_pfp.jpg"
            return getStorageInstance().child(path).downloadUrl
        }
    }
}