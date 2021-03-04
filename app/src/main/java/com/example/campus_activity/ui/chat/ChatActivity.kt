package com.example.campus_activity.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar:Toolbar
    private lateinit var messageEditText: TextInputEditText
    private lateinit var sendButton:FloatingActionButton
    private lateinit var recyclerView:RecyclerView
    private lateinit var recyclerViewAdapter: ChatAdapter
    private var chats:ArrayList<ChatModel> = ArrayList()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.chat_toolbar)
        messageEditText = findViewById(R.id.message_edit_text)
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

        window.setBackgroundDrawable(resources.getDrawable(R.drawable.background))

        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(bottom < oldBottom){
                recyclerView.smoothScrollToPosition(bottom)
            }
        }

        sendButton.setOnClickListener {
            if(messageEditText.text.toString() != ""){
                insertChatOnClick(messageEditText.text.toString())
                messageEditText.setText("")
            }
        }
    }

    private fun insertRandomChats(){
        for(i in 0..3){
            insertChat("Someone", "This is a message!", Timestamp(Date.from(Instant.now())))
            insertChat("You", "This is my message!", Timestamp(Date.from(Instant.now())))
            recyclerViewAdapter.addChat()
        }
        recyclerView.scrollToPosition(chats.size - 1)
    }

    private fun insertChat(sender:String, message:String, timestamp: Timestamp): ChatModel {
        val newChat = ChatModel(sender, message, timestamp)
        chats.add(newChat)
        return newChat
    }

    private fun insertChatOnClick(message:String){
        insertChat("You", message, Timestamp(Date.from(Instant.now())))
        recyclerViewAdapter.addChat()
        recyclerView.scrollToPosition(chats.size - 1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.random_chats -> {
                insertRandomChats()
                true
            }
            else -> false
        }
    }

}