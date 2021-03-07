package com.example.campus_activity.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Initialize values
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation_bar)

        //  Set drawer toggle
        val drawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
            R.string.app_name,
            R.string.app_name
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

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