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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomListFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var roomsAdapter: RoomAdapter
    @Inject
    lateinit var roomsRepository: RoomsRepository

    private var user: FirebaseUser? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnToClub : FloatingActionButton
    private var rooms:List<RoomModel> = ArrayList()
    private lateinit var allRooms:LiveData<List<RoomModel>>

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

    private fun checkAdmin(){
        val email = user?.email!!.toString()
        if(email.substring(0, 3) == "adm" && email.substring(4, 8) == "0000"){
            isAdmin = true
        }
    }

    override fun onResume() {
        fetchingData()
        super.onResume()
    }

    private fun fetchingData()
    {
        roomsRepository.getAllRooms()
        allRooms.observe(viewLifecycleOwner, {
            if(isAdmin){
                rooms = it
                roomsAdapter.setFeed(it)
            }
            else{
                val email = user?.email!!.toString()
                val tempRooms = ArrayList<RoomModel>()
                it.map { r ->
                    val checkString = r.members?.find {s ->
                        s == user?.email
                    }
                    if(r.admin == email || checkString == user?.email){
                        tempRooms.add(r)
                    }
                }
                rooms = tempRooms
                roomsAdapter.setFeed(tempRooms)
            }
        })
    }
}