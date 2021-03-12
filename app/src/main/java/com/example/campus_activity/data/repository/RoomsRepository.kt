package com.example.campus_activity.data.repository

import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.RoomModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.Exception

class RoomsRepository
constructor(
    val firebaseFirestore: FirebaseFirestore
){
    var allRooms:MutableStateFlow<List<RoomModel>> = MutableStateFlow(ArrayList())

    fun insertRoom(roomName:String, admin:String, members:ArrayList<String>){
        val members = hashMapOf<String, Any>()
        members["admin"] = admin
        members["members"] = members
        members["name"] = roomName
        firebaseFirestore.collection("rooms")
            .add(members)
    }

    fun getAllRooms(){
        CoroutineScope(Dispatchers.IO).launch {
            val snaps = firebaseFirestore.collection("rooms")
                .get()
                .await()

            val list:ArrayList<RoomModel> = ArrayList()
            snaps.documents.map {
                try {
                    val room = getRoom(it)
                    list.add(room)
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }

            allRooms.emit(list)
        }
    }

    private fun getRoom(snap:DocumentSnapshot):RoomModel{
        return RoomModel(
            snap.id,
            snap["name"] as String,
            snap["admin"] as String,
            snap["members"] as List<String>,
            "Some random last message",
            Timestamp.now()
        )
    }
}