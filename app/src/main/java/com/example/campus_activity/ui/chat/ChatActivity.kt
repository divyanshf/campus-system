package com.example.campus_activity.ui.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.ui.adapter.ChatAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar:Toolbar
    private lateinit var sendButton:FloatingActionButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter: ChatAdapter
    private var chats:ArrayList<ChatModel> = ArrayList()

    init {
        for(i in 0..10){
            insertChat()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.chat_toolbar)
        sendButton = findViewById(R.id.send_message_button)
        recyclerView = findViewById(R.id.chat_recycler_view)
        recyclerViewAdapter = ChatAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = recyclerViewAdapter

        Log.i("Adapter", "Set notes")
        recyclerViewAdapter.setChats(chats)
        recyclerView.scrollToPosition(chats.size - 1)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sendButton.setOnClickListener {
            insertChatOnClick()
        }
    }

    private fun insertChat(): ChatModel {
        val newChat = ChatModel("Someone", "This is a message!", Timestamp(Date.from(Instant.now())))
        chats.add(newChat)
        return newChat
    }

    private fun insertChatOnClick(){
        recyclerViewAdapter.addChat(insertChat())
        recyclerView.scrollToPosition(chats.size - 1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                Toast.makeText(this, "Back to home", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}