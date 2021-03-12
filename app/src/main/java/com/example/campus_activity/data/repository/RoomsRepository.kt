package com.example.campus_activity.data.repository

import com.example.campus_activity.data.model.RoomModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
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
        val members = hashMapOf(
            "admin" to admin,
            "members" to members,
            "name" to roomName
        )
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
            snap["members"] as ArrayList<String>,
            "Some random last message",
            Timestamp.now()
        )
    }
}