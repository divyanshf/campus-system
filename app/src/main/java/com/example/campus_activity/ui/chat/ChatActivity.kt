package com.example.campus_activity.ui.chat

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.viewmodels.ChatsViewModel
import com.example.campus_activity.ui.adapter.ChatAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), ChatAdapter.OnReceiverItemLongClick, ChatAdapter.OnSenderItemLongClick {

    private var roomName = "test01"
    private var roomId = "test01"
    private var room:RoomModel? = null
    private var isAdmin = false

    //  Hilt variables
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
    private lateinit var recyclerViewAdapter: ChatAdapter
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
            roomId = room?.id!!
            if(room?.admin == user?.email.toString()){
                isAdmin = true
            }
            //  testEndPoint = chatsReference.child(roomName)
        }catch (e:Exception){
            Toast.makeText(this, "Unidentified room!", Toast.LENGTH_SHORT).show()
        }

        chatsViewModel = ChatsViewModel(roomId)

        //  Variable assignment
        toolbar = findViewById(R.id.chat_toolbar)
        progressBar = findViewById(R.id.chat_progress_bar)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_message_button)
        dateMaterialCard = findViewById(R.id.chat_date_mat_card)
        dateTextView = findViewById(R.id.chat_date_text_view)
        recyclerView = findViewById(R.id.chat_recycler_view)
        recyclerViewAdapter = ChatAdapter(this, this, this)
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
            if(messageEditText.text?.isNotBlank() == true){
                insertChatOnClick(messageEditText.text.toString())
                messageEditText.setText("")
            }
        }

        addListener()
    }

    //  Add listener
    @ExperimentalTime
    private fun addListener(){
        //  Add chat listener
        chatsViewModel.allChats.observe(this, {
            Log.i("Change", "Observed")
            when (it) {
                is Result.Progress -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success.ChatLoad -> {
                    progressBar.visibility = View.INVISIBLE

                    val tmpArray = it.result as ArrayList<ChatModel>

                    chats = tmpArray
                    recyclerViewAdapter.setChats(tmpArray)

                    recyclerView.scrollToPosition(chats.size - 1)
                    handleDateCard()
                }
                is Result.Success.ChatAdd -> {
                    progressBar.visibility = View.INVISIBLE

                    val tmpArray = it.result as ArrayList<ChatModel>

                    for( chat in tmpArray ){
                        chats.add(chat)
                        recyclerViewAdapter.addChat(chat)
                    }

                    recyclerView.scrollToPosition(chats.size - 1)
                    handleDateCard()
                }
                is Result.Error -> {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "This shouldn't have happened!", Toast.LENGTH_SHORT).show()
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

                //  Handle fab icon
                if(currentBottomPosition == chats.size - 1){
                    fabScrollToBottom.hide()
                }
                else{
                    fabScrollToBottom.show()
                }

                handleDateCard()

                super.onScrolled(recyclerView, dx, dy)
            }
        })
        fabScrollToBottom.setOnClickListener {
            recyclerView.smoothScrollToPosition(recyclerView.bottom)
        }
    }

    @ExperimentalTime
    private fun handleDateCard(){
        val currentCompleteTopPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        val currentTopPosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        //  Handle date text
        if (currentCompleteTopPosition == 0){
            dateMaterialCard.visibility = View.INVISIBLE
        }
        else{
            dateMaterialCard.visibility = View.VISIBLE
        }

        try {
            val day = recyclerViewAdapter.getDay(chats[currentTopPosition].timestamp)
            dateTextView.text = day
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //  Insert message by "You"
    private fun insertChatOnClick(message:String){
        if(message != ""){
            chatsViewModel.insertChatOnClick(message)
        }
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

    private fun report(position: Int){
        Log.i("Forward", position.toString())
    }

    private fun unsend(position: Int){
        MaterialAlertDialogBuilder(this)
            .setTitle("Unsend")
            .setMessage("Are you sure ?")
            .setPositiveButton("Yes"){ _: DialogInterface, _: Int ->
                chatsViewModel.deleteChat(chats[position])
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun copy(position: Int){
        val chat = chats[position]
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(chat.id, chat.message)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Message copied", Toast.LENGTH_SHORT).show()
    }

    override fun onReceiverItemLongClickHandler(position: Int, view: View?) {
        val items = arrayOf("Unsend", "Copy")

        val dialog = MaterialAlertDialogBuilder(this)
            .setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text2, items) as ListAdapter){ _: DialogInterface, i: Int ->
                when(i){
                    0 -> unsend(position)
                    1 -> copy(position)
                }
            }
        dialog.show()
    }

    override fun onSenderItemLongClickHandler(position: Int, view: View?) {
        val items = arrayOf("Copy")

        val dialog = MaterialAlertDialogBuilder(this)
            .setAdapter(ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text2, items) as ListAdapter){ _: DialogInterface, i: Int ->
                when(i){
                    0 -> copy(position)
                }
            }

        dialog.show()

    }

}