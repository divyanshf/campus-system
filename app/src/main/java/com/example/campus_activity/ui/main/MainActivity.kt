
package com.example.campus_activity.ui.main

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.campus_activity.R
import com.example.campus_activity.ui.auth.AuthActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout

    private lateinit var navView:NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  Initialize values
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_drawer)
        val data = intent.getStringExtra("skip")
        if(data == "true"){
            navView = findViewById<View>(R.id.nav_drawer) as NavigationView
            val nav_Menu: Menu = navView.getMenu()
            nav_Menu.findItem(R.id.iclogout).isVisible = false
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener{ item ->
            onNavItemSelect(item)
        }

        //  Initialize
        initialize()

        //  Set action bar
        setSupportActionBar(toolbar)
    }

    //  Initialize
    private fun initialize(){
        navView.setCheckedItem(R.id.ichome)
        onNavItemSelect(navView.menu.getItem(0))
    }

    //  On navigation item select
    private fun onNavItemSelect(item: MenuItem) : Boolean{
        invalidateOptionsMenu()
        return when (item.itemId) {
            R.id.ichome -> {
                val homeFragment = HomeFragment()
                startFragment(homeFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.icchat -> {
                val chatRoomFragment = RoomListFragment()
                startFragment(chatRoomFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            R.id.iclogout -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                        firebaseAuth.signOut()
                        val intent = Intent(this, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    .setNegativeButton("No", null)
                    .show()
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
            else -> false
        }
    }

    //  Start a fragment
    private fun startFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()
    }

    //  Menu options
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }



}


