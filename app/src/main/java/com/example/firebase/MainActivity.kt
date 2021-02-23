package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.button)


    }
    public final fun clicked(view: View)
    {
        val intent = Intent(this, login::class.java)
        startActivity(intent)
    }
    public final fun tosignup(view: View)
    {
        val intent = Intent(this, signup::class.java)
        startActivity(intent)
    }
}