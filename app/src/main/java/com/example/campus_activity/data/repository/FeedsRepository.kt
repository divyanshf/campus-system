package com.example.campus_activity.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.campus_activity.data.model.FeedModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FeedsRepository(
    val firebaseFirestore: FirebaseFirestore
) {
    val allFeeds: MutableStateFlow<List<FeedModel>> = MutableStateFlow(ArrayList())

    fun insertFeed(feedModel: FeedModel){
        firebaseFirestore.collection("feeds")
            .add(feedModel)
    }

    fun getAllFeeds(){
        CoroutineScope(Dispatchers.IO)
            .launch {
                val snaps = firebaseFirestore.collection("feeds")
                    .orderBy("timestamp")
                    .get()
                    .await()

                val list:ArrayList<FeedModel> = ArrayList()
                snaps.documents.map {
                    val feed = getFeed(it)
                    list.add(feed)
                }

                allFeeds.emit(list)
            }
    }

    private fun getFeed(snap:DocumentSnapshot) : FeedModel{
        return FeedModel(
            snap.id,
            snap["sender_name"] as String,
            snap["posted_by"] as String,
            snap["post"] as String,
            snap["timestamp"] as Timestamp
        )
    }
}