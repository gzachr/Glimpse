package com.mobdeve.s12.group8.glimpse

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobdeve.s12.group8.glimpse.model.Post
import com.mobdeve.s12.group8.glimpse.model.Reaction
import com.mobdeve.s12.group8.glimpse.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
        const val EMAIL_FIELD = "email"

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

        fun addPost(post: Post): Task<DocumentReference> {
            return getPostCollectionReference().add(post)
        }

        fun addReaction(reaction: Reaction): Task<DocumentReference> {
            return getReactionCollectionReference().add(reaction)
        }

        fun getUserByUsername(username: String): Task<QuerySnapshot> {
            return getUserCollectionReference().whereEqualTo(USERNAME_FIELD, username).get()
        }

        fun getUserByEmail(email : String) : Task<QuerySnapshot> {
            return getUserCollectionReference().whereEqualTo(EMAIL_FIELD, email).get()
        }

        fun getUserByID(userID : String) : Task<DocumentSnapshot>{
            return getUserCollectionReference().document(userID).get()
        }

        fun getDefaultUserPhoto(): Task<Uri> {
            val path = "profile_imgs/default_pfp.jpg"
            return getStorageInstance().child(path).downloadUrl
        }

        suspend fun saveImageToStorage(userId: String, data: ByteArray): Task<Uri> {
            val path = "posts/${userId}/${System.currentTimeMillis()}.jpg"
            getStorageInstance().child(path).putBytes(data).await()

            return getStorageInstance().child(path).downloadUrl
        }

        fun updateUser(userId: String, updates: Map<String, Any>): Task<Void> {
            return getUserCollectionReference().document(userId).update(updates)
        }
    }
}