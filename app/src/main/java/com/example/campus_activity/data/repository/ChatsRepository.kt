package com.example.campus_activity.data.repository

import android.util.Log
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class ChatsRepository
    constructor(
        fireDatabase: FirebaseDatabase,
        val firebaseAuth: FirebaseAuth
    ) {

    private var chatsCount : Long = 0
    private val chatsReference = fireDatabase.getReference("chats")
    private val testEndPoint = chatsReference.child("test")
    private var chats:ArrayList<ChatModel> = ArrayList()
    var allChats:MutableStateFlow<Result<List<ChatModel>>> = MutableStateFlow(Result.Progress)

    init {
        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(chats.size == 0){
                    CoroutineScope(Dispatchers.IO).launch {
                        loadAllChats(snapshot)
                    }
                }
                else if(snapshot.childrenCount.toInt() > chatsCount){
                    CoroutineScope(Dispatchers.IO).launch {
                        addNewChatsOnChange(snapshot)
                    }
                }
                Log.i("Load", "Success")
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        testEndPoint.addValueEventListener(chatListener)
    }

    //  Load chats on startup
    private suspend fun loadAllChats(snapshot : DataSnapshot){
        try {
            val array = snapshot.value as ArrayList<*>
            Log.i("Load", "Loading")
            for(i in array){
                val newChat = convertToChat(i as HashMap<*, *>)
                chats.add(newChat)
            }
            allChats.emit(Result.Success(chats))
        }catch (e:Exception){
            e.printStackTrace()
        }
        chatsCount = snapshot.childrenCount
    }

    //  Add chats on change
    private suspend fun addNewChatsOnChange(snapshot: DataSnapshot){
        try {
            val newChatHash = (snapshot.value as ArrayList<*>)[snapshot.childrenCount.toInt() - 1] as HashMap<*,*>
            val newChat = convertToChat(newChatHash)
            chats.add(newChat)
            Log.i("Load", "Adding")
            allChats.emit(Result.Success(chats))
        }catch (e:Exception){
            e.printStackTrace()
        }
        chatsCount = snapshot.childrenCount
    }

    //  Convert map to chat model
    private fun convertToChat(hashMap: HashMap<*, *>) : ChatModel{
        return ChatModel(
            hashMap["id"] as Long,
            hashMap["sender"] as String,
            hashMap["senderMail"] as String,
            hashMap["message"]as String,
            convertToTimestamp(hashMap["timestamp"] as HashMap<*, *>)
        )
    }

    //  Convert map to timestamp
    private fun convertToTimestamp(hashMap: HashMap<*, *>) : Timestamp {
        return Timestamp(hashMap["seconds"] as Long, (hashMap["nanoseconds"] as Long).toInt())
    }


    fun insertChatToDB(message:String){
        val user = firebaseAuth.currentUser
        val newChat = ChatModel(chatsCount, user?.displayName!!, user.email!!, message, Timestamp.now())
        testEndPoint.child("$chatsCount").setValue(newChat)
    }
}