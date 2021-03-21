package com.example.campus_activity.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.campus_activity.R
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.repository.RoomsRepository
import com.example.campus_activity.data.viewmodels.FeedsViewModel
import com.example.campus_activity.data.viewmodels.RoomsViewModel
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.example.campus_activity.ui.create.NewFeed
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var feedsAdapter: FeedAdapter
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val roomsViewModel: RoomsViewModel by viewModels()
    private val feedsViewModel: FeedsViewModel by viewModels()
    private lateinit var noFeeds: LinearLayout
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var feedsRecyclerView: RecyclerView
    private lateinit var addFeedButton: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        noFeeds = view.findViewById(R.id.no_feeds)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        addFeedButton = view.findViewById(R.id.add_feed)
        progressBar = view.findViewById(R.id.progress_bar)

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

        addFeedButton.setOnClickListener {
            val intent = Intent(requireContext(), NewFeed::class.java)
            startActivity(intent)
        }

        return view
    }

    //  Load feeds
    private fun loadFeeds(){
        feedsViewModel.getAllFeeds()
        feedsViewModel.allFeeds.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Progress -> {
                    progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    feedsAdapter.setFeed(it.result)
                    progressBar.visibility = View.GONE
                    if(it.result.isEmpty()){
                        noFeeds.visibility = View.VISIBLE
                    }else{
                        noFeeds.visibility = View.GONE
                    }
                }
                is Result.Error -> {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        })
    }
}