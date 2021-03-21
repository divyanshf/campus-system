package com.example.campus_activity.data.model

import com.google.firebase.Timestamp

data class FeedModel(
    var posted_by: String,
    var post: String,
    var roomModel: RoomModel,
    var uri:String?,
    var timestamp: Timestamp
    )