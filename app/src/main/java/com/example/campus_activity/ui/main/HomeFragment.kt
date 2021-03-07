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
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var feedsAdapter: FeedAdapter

    private lateinit var feedsRecyclerView: RecyclerView
    private var feeds:List<FeedModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        feedsRecyclerView = view.findViewById(R.id.feeds_recycler_view)
        feedsRecyclerView.adapter = feedsAdapter

        feedsRecyclerView.layoutManager = LinearLayoutManager(context)

        //  Set up feeds
        feeds = generateDummyList(10)
        feedsAdapter.setFeed(feeds)

        return view
    }

    private fun generateDummyList(size: Int): List<FeedModel> {
        val list = ArrayList<FeedModel>()
        val text = "Contrary to popular belief Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old."
        for (i in 0 until size) {
            val item = FeedModel("XXXYYYY", "Abhigyan Abhikaushalam Students Forum", "Posted by Yuvaan", text, Timestamp.now())
            list += item
        }
        return list
    }
}