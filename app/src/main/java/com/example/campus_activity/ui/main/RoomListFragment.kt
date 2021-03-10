package com.example.campus_activity.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.repository.RoomsRepository
import com.example.campus_activity.ui.adapter.RoomAdapter
import com.example.campus_activity.ui.chat.NewClub
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomListFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var roomsAdapter: RoomAdapter
    @Inject
    lateinit var roomsRepository: RoomsRepository

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnToClub : FloatingActionButton
    private var rooms:List<RoomModel> = ArrayList()
    private lateinit var allRooms:LiveData<List<RoomModel>>

    //  Is the user college admin
    private val isAdmin = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_list, container, false)

        btnToClub= view.findViewById(R.id.fabToNewClub)
        allRooms = roomsRepository.allRooms.asLiveData()

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

        fetchingData()

        return view
    }

    override fun onResume() {
        fetchingData()
        super.onResume()
    }

    private fun fetchingData()
    {
        roomsRepository.getAllRooms()
        allRooms.observe(viewLifecycleOwner, {
            rooms = it
            roomsAdapter.setFeed(it)
        })
    }
}