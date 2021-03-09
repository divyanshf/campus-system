package com.example.campus_activity.ui.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.campus_activity.R
import com.example.campus_activity.ui.main.MainActivity
import com.example.campus_activity.ui.main.RoomListFragment

class newClub : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_club)




        val clubEdt : EditText= findViewById(R.id.club_Name)
        val adminEdt : EditText = findViewById(R.id.admin_Roll)
        val btnAdd: Button = findViewById(R.id.add_New_Club)




        btnAdd.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}

