package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class mainscreen : AppCompatActivity() {

    lateinit var homeFragment: HomeFragment
    lateinit var chatroomFragment: ChatroomFragment
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainscreen)

        //setting up action bar
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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