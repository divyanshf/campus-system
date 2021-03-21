package com.example.campus_activity.data.repository

import android.net.Uri
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedsRepository
    constructor(
    val firebaseFirestore: FirebaseFirestore,
    val firebaseStorage: FirebaseStorage
) {
    val allFeeds: MutableStateFlow<Result<List<FeedModel>>> = MutableStateFlow(Result.Progress)

    fun insertFeed(feedModel: FeedModel){
        firebaseFirestore.collection("feeds")
            .add(feedModel)
    }

    fun uploadImage(path:String, imageUri: Uri) = flow<Result<Uri>>{
        val storageRef = firebaseStorage.reference.child(path)
        emit(Result.Progress)

        storageRef.putFile(imageUri)
            .await()

        val downloadUri = storageRef.downloadUrl
            .await()

        emit(Result.Success(downloadUri))
    }

    suspend fun getAllFeeds(){
        val snaps = firebaseFirestore.collection("feeds")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        val list:ArrayList<FeedModel> = ArrayList()
        snaps.documents.map {
            val feed = getFeed(it)
            list.add(feed)
        }

        allFeeds.emit(Result.Success(list))
    }

    private fun getFeed(snap:DocumentSnapshot) : FeedModel{
        return FeedModel(
            snap["sender_name"] as String,
            snap["posted_by"] as String,
            snap["post"] as String,
            snap["uri"] as String?,
            snap["timestamp"] as Timestamp
        )
    }
}