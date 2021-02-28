package com.example.campus_activity.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.campus_activity.R
import com.google.android.material.bottomappbar.BottomAppBar

class ChatActivity : AppCompatActivity() {
    private lateinit var toolbar:Toolbar
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        toolbar = findViewById(R.id.chat_toolbar)
        bottomAppBar = findViewById(R.id.chat_bottom_app_bar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}