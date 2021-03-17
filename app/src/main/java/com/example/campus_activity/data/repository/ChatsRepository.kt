package com.example.campus_activity.data.repository

import android.util.Log
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatsRepository
constructor(
    private val roomName: String
) {

    private var chatsCount : Long = 0
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var fireDatabase = FirebaseDatabase.getInstance()
    private lateinit var testEndPoint : DatabaseReference
    private var chats:ArrayList<ChatModel> = ArrayList()
    val allChats:MutableStateFlow<Result<List<ChatModel>>> = MutableStateFlow(Result.Progress)

    init {
        initialize()
    }

    //  Initialize
    private fun initialize(){
        testEndPoint = fireDatabase.getReference("chats").child(roomName)

        val chatListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
//                    if(chats.size == 0){
                        loadAllChats(snapshot)
//                    }
//                    else if(snapshot.childrenCount.toInt() > chatsCount){
//                        addNewChatsOnChange(snapshot)
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        testEndPoint.addValueEventListener(chatListener)
    }

    //  Load chats on startup
    private suspend fun loadAllChats(snapshot : DataSnapshot){
        try {
            val array = (snapshot.value as HashMap<*, *>).toList()
            chats.clear()
            for(i in array){
                val id = i.first
                val chat = i.second
                val newChat = convertToChat(id.toString(), chat as HashMap<*,*>)
                chats.add(newChat)
            }
            chats.sortBy {
                it.timestamp
            }
            Log.i("Chats", chats.toString())
            allChats.emit(Result.Success(chats))
        }catch (e:Exception){
            e.printStackTrace()
            allChats.emit(Result.Error("Something went wrong"))
        }
        chatsCount = snapshot.childrenCount
    }

    //  Add chats on change
    private suspend fun addNewChatsOnChange(snapshot: DataSnapshot){
        try {
            Log.i("Add", "try")
            val array = (snapshot.value as HashMap<*,*>).toList()
            val newId = array[snapshot.childrenCount.toInt() - 1].first.toString()
            val newChatHash = array[snapshot.childrenCount.toInt() - 1].second as HashMap<*,*>
            val newChat = convertToChat(newId, newChatHash)
            chats.add(newChat)
            allChats.emit(Result.Success(chats))
            Log.i("Add", chats[chats.size - 1].toString())
        }catch (e:Exception){
            Log.i("Add", "error")
            e.printStackTrace()
        }
        chatsCount = snapshot.childrenCount
    }

    //  Convert map to chat model
    private fun convertToChat(id:String, hashMap: HashMap<*, *>) : ChatModel{
        return ChatModel(
            id,
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
        val newChat = ChatModel("", user?.displayName!!, user.email!!, message, Timestamp.now())
        testEndPoint.push().setValue(newChat)
    }
}