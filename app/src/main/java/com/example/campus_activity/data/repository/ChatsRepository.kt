package com.example.campus_activity.data.repository

import android.util.Log
import android.widget.Toast
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ChatsRepository
constructor(
    val firestore: FirebaseFirestore,
    val firebaseAuth: FirebaseAuth
) {

    private var chatReference:CollectionReference? = null
    private val _allChats:MutableStateFlow<Result<List<ChatModel>>> = MutableStateFlow(Result.Progress)
    val allChats:StateFlow<Result<List<ChatModel>>> = _allChats

    fun initialize(roomId:String) {
        chatReference = firestore.collection("rooms").document(roomId).collection("chats")
        chatReference!!
            .addSnapshotListener { value, _ ->
                var remove = false
                try {
                    for (chats in value?.documentChanges!!){
                        if (chats.type == DocumentChange.Type.REMOVED){
                            remove = true
                            break
                        }
                    }
                    if (!remove){
                        val changes = createArrayFromChanges(value.documentChanges)
                        Log.i("Changes", value.documentChanges[0].type.toString())
                        changes.sortBy {
                            it.timestamp
                        }
                        _allChats.value = Result.Success.ChatAdd(changes)
                    }
                    else{
                        val array = createArrayFromSnaps(value.documents)
                        array.sortBy {
                            it.timestamp
                        }
                        _allChats.value = Result.Success.ChatLoad(array)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                    _allChats.value = Result.Error("Empty Chat")
                }
            }
    }

    fun insertChatToDB(message:String){
        val user = firebaseAuth.currentUser
        val newChat = ChatModel("", user?.displayName!!, user.email!!, message, Timestamp.now())
        chatReference!!
            .add(createMapFromChat(newChat))
    }

    fun deleteChat(chatModel: ChatModel){
        chatReference!!
            .document(chatModel.id)
            .delete()
    }

    private suspend fun findMember(roomName: String, email: String) : Boolean{
        val snap = firestore.collection("rooms")
            .whereEqualTo("name", roomName)
            .whereArrayContains("members", email)
            .get()
            .await()

        return snap.documents.size != 0
    }

    private suspend fun checkAdmin(roomName: String, email: String) : Boolean{
        val snap = firestore.collection("rooms")
            .whereEqualTo("name", roomName)
            .whereEqualTo("admin", email)
            .get()
            .await()

        return snap.documents.size != 0
    }

    fun addMember(roomId:String, roomName:String, email:String) = flow<Result<Boolean>>{
        var find = findMember(roomName, email)
        if(!find){
            find = checkAdmin(roomName, email)
        }

        if(!find){
            firestore.collection("rooms")
                .document(roomId)
                .update("members", FieldValue.arrayUnion(email))
                .await()

            emit(Result.Success.Success(true))
        }
        else{
            emit(Result.Success.Success(false))
        }
    }

    private fun createArrayFromSnaps(snapList: MutableList<DocumentSnapshot>?):ArrayList<ChatModel>{
        val chats = ArrayList<ChatModel>()

        if (snapList != null) {
            for(i in snapList){
                val note = ChatModel(
                    i.id,
                    i["sender"] as String,
                    i["senderMail"] as String,
                    i["message"] as String,
                    i["timestamp"] as Timestamp
                )
                chats.add(note)
            }
        }

        return chats
    }

    private fun createArrayFromChanges(snapList: MutableList<DocumentChange>?):ArrayList<ChatModel>{
        val chats = ArrayList<ChatModel>()

        if (snapList != null) {
            for(i in snapList){
                if(i.type == DocumentChange.Type.ADDED){
                    val note = ChatModel(
                        i.document.id,
                        i.document["sender"] as String,
                        i.document["senderMail"] as String,
                        i.document["message"] as String,
                        i.document["timestamp"] as Timestamp
                    )
                    chats.add(note)
                }
            }
        }

        return chats
    }

    private fun createMapFromChat(chat:ChatModel):HashMap<String, Any>{
        return hashMapOf(
            "sender" to chat.sender,
            "senderMail" to chat.senderMail,
            "message" to chat.message,
            "timestamp" to chat.timestamp
        )
    }
}