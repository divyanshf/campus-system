package com.example.campus_activity.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import org.jetbrains.annotations.NotNull

@Entity(tableName = "chats_table")
data class ChatModel(
    @PrimaryKey(autoGenerate = true) @NotNull var id:Long,

    @NotNull var sender:String,

    @NotNull var senderMail:String,

    @NotNull var message:String,

    var timestamp: Timestamp
){
    constructor(sender:String, senderMail: String, message:String, timestamp: Timestamp) : this(0, sender, senderMail, message, timestamp)
}