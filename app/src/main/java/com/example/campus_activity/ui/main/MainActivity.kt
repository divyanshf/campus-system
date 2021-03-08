package com.example.campus_activity.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.campus_activity.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Initialize values
        drawerLayout = findViewById(R.id.drawer_layout)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                drawerLayout.openDrawer(START)
                true
            }
            else -> false
        }
    }
}