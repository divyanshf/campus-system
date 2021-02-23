package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class mainscreen : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var chatroomFragment: ChatroomFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscreen)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottomnav)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){

                R.id.home -> {
                    homeFragment= HomeFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment,homeFragment)
                    transaction.commit()



                }
                R.id.chatroom -> {
                    chatroomFragment=ChatroomFragment()
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment,chatroomFragment)
                    transaction.commit()
                }


            }
            true


        }



    }

    
}