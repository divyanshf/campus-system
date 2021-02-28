package com.example.campus_activity.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.ChatModel

class ChatAdapter
constructor(
        context: Context
) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val mInflater = LayoutInflater.from(context)
    private var chats:ArrayList<ChatModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = mInflater.inflate(R.layout.card_message, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentChat = chats[position]
        holder.senderView.text = currentChat.sender
        holder.messageView.text = currentChat.message
        holder.dateView.text = currentChat.timestamp.toString()
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun setChats(chatsList:List<ChatModel>){
        chats = chatsList as ArrayList<ChatModel>
        notifyDataSetChanged()
    }

    fun addChat(chatItem:ChatModel){
//        chats.add(chatItem)
        Log.i("Adapter", "Add notes")
        notifyItemInserted(chats.size - 1)
    }

    inner class ChatViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        var senderView: TextView = itemView.findViewById(R.id.sender_name)
        var messageView:TextView = itemView.findViewById(R.id.message_text_view)
        var dateView:TextView = itemView.findViewById(R.id.time_text_view)
    }

}