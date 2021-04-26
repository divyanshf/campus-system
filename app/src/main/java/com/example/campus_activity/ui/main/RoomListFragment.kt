package com.example.campus_activity.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_activity.R
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.viewmodels.RoomsViewModel
import com.example.campus_activity.ui.adapter.RoomAdapter
import com.example.campus_activity.ui.create.NewClub
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

@AndroidEntryPoint
class RoomListFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var roomsAdapter: RoomAdapter

    private var user: FirebaseUser? = null
    private val roomsViewModel:RoomsViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar:ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var notMember: LinearLayout
    private lateinit var btnToClub : FloatingActionButton
    private var rooms:List<RoomModel> = ArrayList()

    //  Is the user college admin
    private var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_list, container, false)

        user = firebaseAuth.currentUser
        checkAdmin()

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        progressBar = view.findViewById(R.id.progress_bar)
        notMember = view.findViewById(R.id.not_member)
        btnToClub= view.findViewById(R.id.fabToNewClub)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = roomsAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        //  IS USER ADMIN
        if(!isAdmin){
            btnToClub.visibility = View.GONE
        }else{
            btnToClub.setOnClickListener{
                val intent = Intent(activity, NewClub::class.java)
                startActivity(intent)
            }
        }


        swipeRefreshLayout.setOnRefreshListener {
            fetchingData()
            Timer("Refresh", false).schedule(500){
                swipeRefreshLayout.isRefreshing = false
            }
        }

        fetchingData()

        return view
    }

    private fun checkAdmin(){
        try {
            val email = user?.email!!.toString()
            if(email.substring(0, 3) == "adm" && email.substring(4, 8) == "0000"){
                isAdmin = true
            }
        }catch (e:Exception){
            Toast.makeText(context, "Unauthenticated user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchingData()
    {
        roomsViewModel.getAllRooms()
        roomsViewModel.allRooms.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Progress -> {
                    progressBar.visibility = View.VISIBLE
                    notMember.visibility = View.GONE
                }
                is Result.Success -> {
                    Log.i("Success", it.result.toString())
                    progressBar.visibility = View.GONE
                    rooms = it.result
                    roomsAdapter.setRooms(it.result)
                    if(rooms.isEmpty()){
                        notMember.visibility = View.VISIBLE
                    }
                    else{
                        notMember.visibility = View.GONE
                    }
                }
                else -> {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}