package com.example.campus_activity.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.ui.adapter.ChatAdapter
import com.example.campus_activity.ui.main.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URI
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@RequiresApi(Build.VERSION_CODES.O)
class ChatActivity : AppCompatActivity() {

    //  Test realtime database
    private val fireDatabase = FirebaseDatabase.getInstance()
    private var chatsCount : Long = 0
    private val chatsReference = fireDatabase.getReference("chats")
    private val testEndPoint = chatsReference.child("test")

    //  Variable declaration
    private lateinit var toolbar:Toolbar
    private lateinit var progressBar:ProgressBar
    private lateinit var messageEditText: TextInputEditText
    private lateinit var sendButton:FloatingActionButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter: ChatAdapter
    private var chats:ArrayList<ChatModel> = ArrayList()
    private var backgroundUri:Uri? = null
    private var backgroundPath: String? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //  Variable assignment
        toolbar = findViewById(R.id.chat_toolbar)
        progressBar = findViewById(R.id.chat_progress_bar)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_message_button)
        recyclerView = findViewById(R.id.chat_recycler_view)
        recyclerViewAdapter = ChatAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

        //  Chat realtime listener
        val chatListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(chats.size == 0){
                    loadAllChats(snapshot)
                }
                else if(snapshot.childrenCount.toInt() > chatsCount){
                    addNewChatsOnChange(snapshot)
                }
                chatsCount = snapshot.childrenCount
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        testEndPoint.addValueEventListener(chatListener)

        //  Initialize action bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //  Set the background of the chat
        window.setBackgroundDrawable(resources.getDrawable(R.drawable.background))

        //  Scroll to bottom on keyboard pop
        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(bottom < oldBottom){
                recyclerView.smoothScrollToPosition(bottom)
            }
        }

        //  Send message
        sendButton.setOnClickListener {
            if(messageEditText.text.toString() != ""){
                insertChatOnClick(messageEditText.text.toString())
                messageEditText.setText("")
            }
        }
    }

    //  Load chats on startup
    private fun loadAllChats(snapshot : DataSnapshot){
        try {
            val array = snapshot.value as ArrayList<*>
            for(i in array){
                val newChat = convertToChat(i as HashMap<*, *>)
                insertChat(newChat)
            }
            recyclerViewAdapter.setChats(chats)
            recyclerView.scrollToPosition(chats.size - 1)
        }catch (e:Exception){
            e.printStackTrace()
        }
        progressBar.visibility = View.INVISIBLE
    }

    //  Add chats on change
    private fun addNewChatsOnChange(snapshot: DataSnapshot){
        try {
            val newChatHash = (snapshot.value as ArrayList<*>)[snapshot.childrenCount.toInt() - 1] as HashMap<*,*>
            val newChat = convertToChat(newChatHash)
            insertChat(newChat)
            recyclerViewAdapter.addChat()
            recyclerView.scrollToPosition(chats.size - 1)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //  Convert map to chat model
    private fun convertToChat(hashMap: HashMap<*, *>) : ChatModel{
        return ChatModel(
                hashMap["id"] as Long,
                hashMap["sender"] as String,
                hashMap["message"]as String,
                convertToTimestamp(hashMap["timestamp"] as HashMap<*, *>)
        )
    }

    //  Convert map to timestamp
    private fun convertToTimestamp(hashMap: HashMap<*, *>) : Timestamp{
        return Timestamp(hashMap["seconds"] as Long, (hashMap["nanoseconds"] as Long).toInt())
    }

    //  Insert chat by model
    private fun insertChat(chat:ChatModel): ChatModel {
        chats.add(chat)
        recyclerView.scrollToPosition(chats.size - 1)
        return chat
    }

    //  Insert message by "You"
    private fun insertChatOnClick(message:String){
        val time = Timestamp(Date.from(Instant.now()))
        val newChat = ChatModel(chatsCount,"You", message, time)

        //  Test database
        testEndPoint.child("$chatsCount").setValue(newChat)
    }

    //  Options create
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //  Option selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.change_background -> {
                true
            }
            else -> false
        }
    }

}