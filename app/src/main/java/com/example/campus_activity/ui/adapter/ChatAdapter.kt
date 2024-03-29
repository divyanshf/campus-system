package com.example.campus_activity.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

class ChatAdapter
constructor(
    val context: Context,
    val receiverListener: OnReceiverItemLongClick,
    val senderListener: OnSenderItemLongClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var firebaseAuth = FirebaseAuth.getInstance()

    private val VIEW_TYPE_SENDER = 1
    private val VIEW_TYPE_RECEIVER = 2
    private val VIEW_TYPE_DATE_SENDER = 3
    private val VIEW_TYPE_DATE_RECEIVER = 4
    private val mInflater = LayoutInflater.from(context)
    private var chats:ArrayList<ChatModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView:View
        return when(viewType){
            VIEW_TYPE_SENDER -> {
                itemView = mInflater.inflate(R.layout.card_sender_message, parent, false)
                SenderChatViewHolder(itemView)
            }
            VIEW_TYPE_RECEIVER -> {
                itemView = mInflater.inflate(R.layout.card_receiver_message, parent, false)
                ReceiverChatViewHolder(itemView)
            }
            VIEW_TYPE_DATE_SENDER -> {
                itemView = mInflater.inflate(R.layout.card_sender_date_message, parent, false)
                SenderDateChatViewHolder(itemView)
            }
            else -> {
                itemView = mInflater.inflate(R.layout.card_receiver_date_message, parent, false)
                ReceiverDateChatViewHolder(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = chats[position]
        val myMail = firebaseAuth.currentUser?.email

        if(message.senderMail == myMail){
            if(position > 0){
                val prevMessage = chats[position - 1]

                var timestamp = prevMessage.timestamp.toDate().toString()
                val prevDate = timestamp.substring(4, 10) + timestamp.substring(timestamp.length - 4, timestamp.length)

                timestamp = message.timestamp.toDate().toString()
                val date = timestamp.substring(4, 10) + timestamp.substring(timestamp.length - 4, timestamp.length)

                if(prevDate != date){
                    return VIEW_TYPE_DATE_RECEIVER
                }
            }
            else if(position == 0){
                return VIEW_TYPE_DATE_RECEIVER
            }
            return VIEW_TYPE_RECEIVER
        }

        if(position > 0){
            val prevMessage = chats[position - 1]

            var timestamp = prevMessage.timestamp.toDate().toString()
            val prevDate = timestamp.substring(4, 10) + timestamp.substring(timestamp.length - 4, timestamp.length)

            timestamp = message.timestamp.toDate().toString()
            val date = timestamp.substring(4, 10) + timestamp.substring(timestamp.length - 4, timestamp.length)

            if(prevDate != date){
                return VIEW_TYPE_DATE_SENDER
            }
        }
        else if(position == 0){
            return VIEW_TYPE_DATE_SENDER
        }
        return VIEW_TYPE_SENDER
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chats[position]
        when(holder.itemViewType){
            VIEW_TYPE_SENDER -> {
                (holder as SenderChatViewHolder).bind(chat)
            }
            VIEW_TYPE_RECEIVER -> {
                (holder as ReceiverChatViewHolder).bind(chat)
            }
            VIEW_TYPE_DATE_SENDER -> {
                (holder as SenderDateChatViewHolder).bind(chat)
            }
            VIEW_TYPE_DATE_RECEIVER -> {
                (holder as ReceiverDateChatViewHolder).bind(chat)
            }
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setChats(chatsList:List<ChatModel>){
        chats = chatsList as ArrayList<ChatModel>
        notifyDataSetChanged()
    }

    fun addChat(chat:ChatModel){
        chats.add(chat)
        notifyItemInserted(chats.size - 1)
    }

    inner class SenderChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener {
        private var senderView: TextView = itemView.findViewById(R.id.sender_name)
        private var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        private var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        init {
            itemView.setOnLongClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            val name = currentChat.sender.split(" ", ",")[0]

            senderView.text = name
            messageView.text = currentChat.message

            val timestamp = currentChat.timestamp.toDate().toString()

            timeView.text = timestamp.substring(11, 16)
        }

        override fun onLongClick(v: View?): Boolean {
            senderListener.onSenderItemLongClickHandler(adapterPosition, v)
            return true
        }
    }

    inner class ReceiverChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener{
        private var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        private var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        init {
            itemView.setOnLongClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            messageView.text = currentChat.message
            val timestamp = currentChat.timestamp.toDate().toString()

            timeView.text = timestamp.substring(11, 16)

        }

        override fun onLongClick(v: View?): Boolean {
            receiverListener.onReceiverItemLongClickHandler(adapterPosition, v)
            return true
        }
    }

    inner class SenderDateChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener{
        private var dateView:TextView = itemView.findViewById(R.id.date_text_view)
        private var senderView: TextView = itemView.findViewById(R.id.sender_name)
        private var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        private var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        init {
            itemView.setOnLongClickListener(this)
        }

        @ExperimentalTime
        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            val name = currentChat.sender.split(" ", ",")[0]

            senderView.text = name
            messageView.text = currentChat.message

            val timestamp = currentChat.timestamp.toDate().toString()
            timeView.text = timestamp.substring(11, 16)

            val day = getDay(currentChat.timestamp)
            dateView.text = day
        }

        override fun onLongClick(v: View?): Boolean {
            senderListener.onSenderItemLongClickHandler(adapterPosition, v)
            return true
        }
    }

    inner class ReceiverDateChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView), View.OnLongClickListener{
        private var dateView:TextView = itemView.findViewById(R.id.date_text_view)
        private var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        private var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        init {
            itemView.setOnLongClickListener(this)
        }

        @ExperimentalTime
        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            messageView.text = currentChat.message

            val timestamp = currentChat.timestamp.toDate().toString()
            timeView.text = timestamp.substring(11, 16)

            val day = getDay(currentChat.timestamp)
            dateView.text = day
        }

        override fun onLongClick(v: View?): Boolean {
            receiverListener.onReceiverItemLongClickHandler(adapterPosition, v)
            return true
        }
    }

    @ExperimentalTime
    fun getDay(timestamp: Timestamp): String {
        val timeMS = timestamp.toDate().time.milliseconds.toLongMilliseconds()
        val timeCalendar = Calendar.getInstance()
        timeCalendar.timeInMillis = timeMS

        val now = Calendar.getInstance().get(Calendar.DATE)
        return when(timeCalendar.get(Calendar.DATE)){
            now -> {
                "Today"
            }
            (now - 1) -> {
                "Yesterday"
            }
            else -> {
                val timestampString = timestamp.toDate().toString()
                val date = timestampString.substring(4, 10)
                val year = timestampString.substring(timestampString.length - 4, timestampString.length)
                return "$date, $year"
            }
        }
    }

    interface OnReceiverItemLongClick{
        fun onReceiverItemLongClickHandler(position: Int, view: View?)
    }

    interface OnSenderItemLongClick{
        fun onSenderItemLongClickHandler(position: Int, view: View?)
    }
}