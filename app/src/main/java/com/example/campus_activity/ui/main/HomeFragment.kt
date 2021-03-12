package com.example.campus_activity.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.repository.FeedsRepository
import com.example.campus_activity.data.repository.RoomsRepository
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var feedsAdapter: FeedAdapter
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var repository: FeedsRepository
    @Inject
    lateinit var roomsRepository: RoomsRepository

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var feedsRecyclerView: RecyclerView
    private lateinit var addFeedCard : MaterialCardView
    private lateinit var newFeed: TextInputEditText
    private lateinit var spinner: Spinner
    private lateinit var uploadButton:Button
    private lateinit var feeds:LiveData<List<FeedModel>>
    private lateinit var rooms:LiveData<List<RoomModel>>

    //  Variable to check if the user is permitted to add feed
    private val canAddFeed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        addFeedCard = view.findViewById(R.id.add_feed_card)
        newFeed = view.findViewById(R.id.new_feed_text_view)
        spinner = view.findViewById(R.id.list_club_spinner)
        uploadButton  = view.findViewById(R.id.add_feed_button)
        feeds  = repository.allFeeds.asLiveData()
        rooms = roomsRepository.allRooms.asLiveData()

        setUpAddFeed()

        feedsRecyclerView = view.findViewById(R.id.feeds_recycler_view)
        feedsRecyclerView.adapter = feedsAdapter

        feedsRecyclerView.layoutManager = LinearLayoutManager(context)

        //  Set up feeds
        loadFeeds()
        swipeRefreshLayout.setOnRefreshListener {
            loadFeeds()
            Timer("Refresh", false).schedule(500){
                swipeRefreshLayout.isRefreshing = false
            }
        }

        return view
    }

    //  Load feeds
    private fun loadFeeds(){
        repository.getAllFeeds()
        feeds.observe(viewLifecycleOwner, {
            feedsAdapter.setFeed(it)
        })
    }

    private fun setUpAddFeed(){
        val user = firebaseAuth.currentUser
        roomsRepository.getAllRooms()
        rooms.observe(viewLifecycleOwner, {
            val testClubs = ArrayList<String>()
            it.map { r ->
                if(user?.email.toString() == r.admin || r.members?.binarySearch(user?.email.toString(), 0, r.members?.size!! - 1)!! >= 0){
                    Log.i("Reached", user?.email.toString())
                    testClubs.add(r.name?.toUpperCase(Locale.ROOT)!!)
                }
            }

            //  Check if member or admin
            if(testClubs.isEmpty()){
                addFeedCard.visibility = View.GONE
            }
            else{
                addFeedCard.visibility = View.VISIBLE
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_gallery_item,
                    testClubs
                )

                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                spinner.adapter = adapter

                uploadButton.setOnClickListener {
                    if(newFeed.text.toString() != "" && spinner.selectedItem.toString() != ""){
                        addFeed(newFeed.text.toString(), spinner.selectedItem.toString())
                        newFeed.setText("")
                    }
                }
            }
        })
    }

    //  Add a new feed
    private fun addFeed(feedText:String, roomName:String){
        val user = firebaseAuth.currentUser
        val feed = FeedModel(roomName, user?.displayName!!, feedText, Timestamp.now())
        repository.insertFeed(feed)
        loadFeeds()
    }
}