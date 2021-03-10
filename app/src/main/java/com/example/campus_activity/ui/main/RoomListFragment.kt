package com.example.campus_activity.ui.main

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.example.campus_activity.ui.adapter.RoomAdapter
import com.example.campus_activity.ui.chat.newClub
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnToClub : FloatingActionButton
    private var rooms:List<RoomModel> = ArrayList()
    private lateinit var db : CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_list, container, false)

        btnToClub= view.findViewById(R.id.fabToNewClub)

        db = FirebaseFirestore.getInstance().collection("rooms")

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = roomsAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        //  Set up feeds
        rooms = generateDummyList(10)
        roomsAdapter.setFeed(rooms)


        //Start newClub Activity

        btnToClub.setOnClickListener{
            
            val intent = Intent(activity, newClub::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        return view
    }

    private fun generateDummyList(size: Int): List<RoomModel> {
        val list = ArrayList<RoomModel>()
        val text = "Contrary to popular belief Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old."
        for (i in 0 until size) {
            val item = RoomModel("XXXYYYY", "Abhigyan Abhikaushalam Students Forum", "I have a message that is ...", Timestamp.now())
            list += item
        }
        return list
    }

    private fun fetchingData()
    {
           db.get().addOnSuccessListener { documents ->
              for (document in documents){
                   Log.i("val", "DocumentSnapshot data: ${document.data}")
               }

           }
               .addOnFailureListener { exception ->
                   Log.i("error", "get failed with ", exception)
               }

    }

}