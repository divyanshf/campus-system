package com.example.campus_activity.ui.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ChatAdapter
constructor(
        context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_SENDER = 1
    private val VIEW_TYPE_RECEIVER = 2
    private val mInflater = LayoutInflater.from(context)
    private var chats:ArrayList<ChatModel> = ArrayList()
    private val name = "You"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView:View
        if(viewType == VIEW_TYPE_SENDER){
            itemView = mInflater.inflate(R.layout.sender_message_card, parent, false)
            return SenderChatViewHolder(itemView)
        }
        else{
            itemView = mInflater.inflate(R.layout.receiver_message_card, parent, false)
            return ReceiverChatViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = chats[position]
        if(message.sender == name){
            return VIEW_TYPE_RECEIVER
        }
        return VIEW_TYPE_SENDER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chats[position]
        if(holder.itemViewType == VIEW_TYPE_SENDER){
            (holder as SenderChatViewHolder).bind(chat)
        }else{
            (holder as ReceiverChatViewHolder).bind(chat)
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setChats(chatsList:List<ChatModel>){
        chats = chatsList as ArrayList<ChatModel>
        notifyDataSetChanged()
    }

    fun addChat(){
        notifyItemInserted(chats.size - 1)
    }

    inner class SenderChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        var senderView: TextView = itemView.findViewById(R.id.sender_name)
        var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        var dateView:TextView = itemView.findViewById(R.id.date_text_view)
        var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            senderView.text = currentChat.sender
            messageView.text = currentChat.message

            val timestamp = currentChat.timestamp.toDate().toString()
            val date = timestamp.substring(4, 10)
            val year = timestamp.substring(timestamp.length - 4, timestamp.length)
            dateView.text = "$date, $year"

            timeView.text = timestamp.substring(11, 16)
        }
    }

    inner class ReceiverChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        var messageCard: MaterialCardView = itemView.findViewById(R.id.message_card)
        var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        var dateView:TextView = itemView.findViewById(R.id.date_text_view)
        var timeView:TextView = itemView.findViewById(R.id.time_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(currentChat:ChatModel){
            messageView.text = currentChat.message
            val timestamp = currentChat.timestamp.toDate().toString()
            val date = timestamp.substring(4, 10)
            val year = timestamp.substring(timestamp.length - 4, timestamp.length)
            dateView.text = "$date, $year"
            timeView.text = timestamp.substring(11, 16)

        }
    }

}