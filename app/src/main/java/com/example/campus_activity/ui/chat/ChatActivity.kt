package com.example.campus_activity.ui.chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.viewmodels.ChatsViewModel
import com.example.campus_activity.ui.adapter.ChatAdapter
import com.example.campus_activity.ui.main.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    //  Test realtime database
//    private val fireDatabase = FirebaseDatabase.getInstance()
//    private var chatsCount : Long = 0
//    private val chatsReference = fireDatabase.getReference("chats")
//    private val testEndPoint = chatsReference.child("test")

    //  Hilt variables
    @Inject
    lateinit var recyclerViewAdapter: ChatAdapter
    private val chatsViewModel:ChatsViewModel by  viewModels()

    //  Variable declaration
    private lateinit var toolbar:Toolbar
    private lateinit var progressBar:ProgressBar
    private lateinit var messageEditText: TextInputEditText
    private lateinit var sendButton:FloatingActionButton
    private lateinit var recyclerView:RecyclerView
    private var chats:ArrayList<ChatModel> = ArrayList()

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter

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

        //  Set listener to chats
        chatsViewModel.allChats.observe(this, {
            when(it){
                Result.Progress -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    progressBar.visibility = View.INVISIBLE

                    chats = it.result as ArrayList<ChatModel>

                    recyclerViewAdapter.setChats(chats)
                    recyclerView.scrollToPosition(chats.size - 1)
                }
                is Result.Error -> {
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })
    }

    //  Insert message by "You"
    private fun insertChatOnClick(message:String){
        chatsViewModel.insertChatOnClick(message)
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