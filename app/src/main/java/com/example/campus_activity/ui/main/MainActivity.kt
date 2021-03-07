package com.example.campus_activity.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.campus_activity.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Initialize values
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_bar)

        //  Set bottom navigation
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.home -> {
                    val homeFragment= HomeFragment()
                    startFragment(homeFragment)
                }
                R.id.chatroom -> {
                    val chatRoomFragment= RoomListFragment()
                    startFragment(chatRoomFragment)
                }
            }
            true
        }

        //  Initialize as home
        bottomNavigation.selectedItemId = R.id.home

        //  Set action bar
        setSupportActionBar(toolbar)

    }

    //  Start a fragment
    private fun startFragment(fragment:Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment,fragment)
        transaction.commit()
    }
    
}