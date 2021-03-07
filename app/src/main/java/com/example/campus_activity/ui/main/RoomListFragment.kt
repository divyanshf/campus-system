package com.example.campus_activity.ui.main

import android.os.Bundle
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
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomListFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var roomsAdapter: RoomAdapter

    private lateinit var recyclerView: RecyclerView
    private var rooms:List<RoomModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_room_list, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = roomsAdapter

        recyclerView.layoutManager = LinearLayoutManager(context)

        //  Set up feeds
        rooms = generateDummyList(10)
        roomsAdapter.setFeed(rooms)

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

}