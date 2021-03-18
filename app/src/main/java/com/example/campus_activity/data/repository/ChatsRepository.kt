package com.example.campus_activity.data.repository

import android.util.Log
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatsRepository
constructor(
    roomId: String
) {

    private val firestoreRef = FirebaseFirestore.getInstance().collection("rooms").document(roomId).collection("chats")
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _allChats:MutableStateFlow<Result<List<ChatModel>>> = MutableStateFlow(Result.Progress)
    val allChats:StateFlow<Result<List<ChatModel>>> = _allChats

    init {
        firestoreRef
            .addSnapshotListener { value, _ ->
                try {
                    val changes = createArrayFromChanges(value?.documentChanges)
                    changes.sortBy {
                        it.timestamp
                    }
                    _allChats.value = Result.Success(changes)
                }catch (e:Exception){
                    e.printStackTrace()
                }
//                val array = createArrayFromSnaps(value?.documents)
//                array.sortBy {
//                    it.timestamp
//                }
            }
    }

    fun insertChatToDB(message:String){
        val user = firebaseAuth.currentUser
        val newChat = ChatModel("", user?.displayName!!, user.email!!, message, Timestamp.now())
        firestoreRef
            .add(createMapFromChat(newChat))
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