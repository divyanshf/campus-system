package com.example.campus_activity.data.model

import com.google.firebase.Timestamp


data class RoomModel(
    var id: String,
    var name: String,
    var admin:String,
    var members:List<String>,
    var lastMessage: String,
    var timestamp: Timestamp
    )