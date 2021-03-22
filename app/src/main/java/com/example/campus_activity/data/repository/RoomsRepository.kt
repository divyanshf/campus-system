package com.example.campus_activity.data.repository

import android.net.Uri
import android.util.Log
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.Exception

class RoomsRepository
constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
){
    var allRooms:MutableStateFlow<Result<List<RoomModel>>> = MutableStateFlow(Result.Progress)

    fun uploadClub(roomName:String, admin:String, members:ArrayList<String>, imageUri: Uri?){
        val members = hashMapOf(
            "admin" to admin,
            "members" to members,
            "name" to roomName,
            "uri" to imageUri.toString()
        )
        firebaseFirestore.collection("rooms")
            .add(members)
            .addOnSuccessListener {
                Log.i("Room Success", it.toString())
            }
    }

    fun uploadImage(path:String, uri: Uri) = flow<Result<Uri>>{
        val storageRef = firebaseStorage.reference.child(path)
        emit(Result.Progress)

        storageRef.putFile(uri)
            .await()

        val downloadUri = storageRef.downloadUrl
            .await()

        emit(Result.Success(downloadUri))
    }

    suspend fun getAllRooms(){
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

        allRooms.emit(Result.Success(list))
    }

    private fun getRoom(snap:DocumentSnapshot):RoomModel{
        return RoomModel(
            snap.id,
            snap["name"] as String,
            snap["admin"] as String,
            snap["members"] as ArrayList<String>,
            Timestamp.now(),
            snap["uri"] as String
        )
    }
}