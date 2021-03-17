package com.example.campus_activity.ui.chat

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.repository.ChatsRepository
import com.example.campus_activity.data.viewmodels.ChatsViewModel
import com.example.campus_activity.ui.adapter.ChatAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    //  Test realtime database
//    private val chatsReference = fireDatabase.getReference("chats")
//    private var testEndPoint = chatsReference.child("test")
    private var roomName = "test01"
    private var room:RoomModel? = null
    private var isAdmin = false

    //  Hilt variables
    @Inject
    lateinit var recyclerViewAdapter: ChatAdapter
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var chatsViewModel : ChatsViewModel

    //  Variable declaration
    private lateinit var toolbar:Toolbar
    private lateinit var progressBar:ProgressBar
    private lateinit var messageEditText: TextInputEditText
    private lateinit var sendButton:FloatingActionButton
    private lateinit var dateMaterialCard:MaterialCardView
    private lateinit var dateTextView:TextView
    private lateinit var recyclerView:RecyclerView
    private lateinit var fabScrollToBottom:FloatingActionButton
    private var chats:ArrayList<ChatModel> = ArrayList()

    @ExperimentalTime
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        //  Add endpoint
        try {
            val user = firebaseAuth.currentUser
            room = intent.getParcelableExtra("room")
            roomName = room?.name!!
            if(room?.admin == user?.email.toString()){
                isAdmin = true
            }
            //  testEndPoint = chatsReference.child(roomName)
        }catch (e:Exception){
            Toast.makeText(this, "Unidentified room!", Toast.LENGTH_SHORT).show()
        }

        chatsViewModel = ChatsViewModel(roomName)

        //  Variable assignment
        toolbar = findViewById(R.id.chat_toolbar)
        progressBar = findViewById(R.id.chat_progress_bar)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_message_button)
        dateMaterialCard = findViewById(R.id.chat_date_mat_card)
        dateTextView = findViewById(R.id.chat_date_text_view)
        recyclerView = findViewById(R.id.chat_recycler_view)
        fabScrollToBottom = findViewById(R.id.foa_scroll_to_bottom)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = recyclerViewAdapter
        fabScrollToBottom.hide()

        //  Initialize action bar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = roomName
        dateMaterialCard.visibility = View.INVISIBLE

        //  Set the background of the chat
        window.setBackgroundDrawable(resources.getDrawable(R.drawable.background))

        //  Scroll to bottom on keyboard pop
        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if(bottom < oldBottom){
                recyclerView.smoothScrollToPosition(bottom)
            }
        }

        setUpScrollToBottom()

        //  Send message
        sendButton.setOnClickListener {
            if(messageEditText.text.toString() != ""){
                insertChatOnClick(messageEditText.text.toString())
                messageEditText.setText("")
            }
        }

        //  Add chat listener
        chatsViewModel.allChats.observe(this, {
            when(it){
                Result.Progress -> {
                    Log.i("UI", "progress")
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    Log.i("UI", "success")
                    progressBar.visibility = View.INVISIBLE

                    chats = it.result as ArrayList<ChatModel>

                    recyclerViewAdapter.setChats(chats)
                    recyclerView.scrollToPosition(chats.size - 1)
                }
                is Result.Error -> {
                    Log.i("UI", "error")
                    progressBar.visibility = View.INVISIBLE
                }
            }
        })
    }

    //  Scroll to bottom
    @ExperimentalTime
    private fun setUpScrollToBottom(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val currentBottomPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val currentCompleteTopPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val currentTopPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                //  Handle fab icon
                if(currentBottomPosition == chats.size - 1){
                    fabScrollToBottom.hide()
                }
                else{
                    fabScrollToBottom.show()
                }

                //  Handle date text
                if (currentCompleteTopPosition == 0){
                    dateMaterialCard.animate().translationY(-100F).alpha(0F)
                }
                else{
                    dateMaterialCard.animate().translationY(0F).alpha(1F)
                }

                val day = recyclerViewAdapter.getDay(chats[currentTopPosition].timestamp)
                dateTextView.text = day

                super.onScrolled(recyclerView, dx, dy)
            }
        })
        fabScrollToBottom.setOnClickListener {
            recyclerView.smoothScrollToPosition(recyclerView.bottom)
        }
    }

    //  Insert message by "You"
    private fun insertChatOnClick(message:String){
        chatsViewModel.insertChatOnClick(message)
    }

    //  Add new member to club
    private fun addNewMember(email:String){
        firebaseFirestore.collection("rooms")
            .document(room?.id!!)
            .update("members", FieldValue.arrayUnion(email))
            .addOnCompleteListener {
                Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show()
            }
    }

    //  Options create
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_chat_activity, menu)
        menu?.findItem(R.id.add_member)?.isVisible = isAdmin
        return super.onCreateOptionsMenu(menu)
    }

    //  Option selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_member -> {
                val dialogBuilder = MaterialAlertDialogBuilder(this)

                val view = this.layoutInflater.inflate(R.layout.dialog_add_member, null)
                dialogBuilder.setView(view)

                val year: TextInputEditText = view.findViewById(R.id.year_edit_text)
                val batch: TextInputEditText = view.findViewById(R.id.batch_edit_text)
                val roll: TextInputEditText = view.findViewById(R.id.roll_edit_text)

                dialogBuilder
                    .setTitle("Add Member")
                    .setPositiveButton("Add") { _: DialogInterface, _: Int ->
                        val yText = year.text.toString()
                        val bText = batch.text.toString()
                        val rText = roll.text.toString()
                        if (yText.length == 4 && bText.length == 3 && rText.length == 3) {
                            val rollNumber = (bText.toLowerCase(Locale.ROOT).plus("_").plus(yText).plus(rText).plus("@iiitm.ac.in"))
                            addNewMember(rollNumber)
                        } else {
                            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Cancel", null)

                val dialog = dialogBuilder.create()
                dialog.show()

                true
            }
            else -> false
        }
    }

}