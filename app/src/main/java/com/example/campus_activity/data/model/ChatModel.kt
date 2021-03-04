package com.example.campus_activity.data.model

import androidx.room.PrimaryKey
import com.google.firebase.Timestamp

data class ChatModel(
        @PrimaryKey(autoGenerate = true) var id:Int,
        var sender:String,
        var message:String,
        var timestamp: Timestamp
){
    constructor(sender:String, message:String, timestamp: Timestamp) : this(0, sender, message, timestamp)
}

