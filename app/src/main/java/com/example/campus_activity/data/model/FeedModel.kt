package com.example.campus_activity.data.model

import com.google.firebase.Timestamp

data class FeedModel(
    var sender_name: String,
    var posted_by: String,
    var post: String,
    var timestamp: Timestamp
    )