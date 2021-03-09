package com.example.campus_activity.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.ui.adapter.FeedAdapter
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    //  Hilt variables
    @Inject
    lateinit var feedsAdapter: FeedAdapter
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var feedsRecyclerView: RecyclerView
    private lateinit var addFeedCard : MaterialCardView
    private lateinit var newFeed: TextInputEditText
    private lateinit var spinner: Spinner
    private lateinit var uploadButton:Button
    private var feeds:ArrayList<FeedModel> = ArrayList()

    //  Variable to check if the user is permitted to add feed
    private val canAddFeed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        addFeedCard = view.findViewById(R.id.add_feed_card)
        newFeed = view.findViewById(R.id.new_feed_text_view)
        spinner = view.findViewById(R.id.list_club_spinner)
        uploadButton  = view.findViewById(R.id.add_feed_button)

        setUpAddFeed()

        feedsRecyclerView = view.findViewById(R.id.feeds_recycler_view)
        feedsRecyclerView.adapter = feedsAdapter

        feedsRecyclerView.layoutManager = LinearLayoutManager(context)

        //  Set up feeds
//        feeds = generateDummyList(10)
        feedsAdapter.setFeed(feeds)

        return view
    }

    private fun setUpAddFeed(){
        if(!canAddFeed){
            addFeedCard.visibility = View.GONE
        }
        else{
            val testClubs = resources.getStringArray(R.array.clubs)
            val adapter : ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.clubs,
                android.R.layout.simple_gallery_item
            )

            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Toast.makeText(context,
                        " " + testClubs[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                    Toast.makeText(context, "Nothing Selected", Toast.LENGTH_SHORT).show()
                }
            }

            uploadButton.setOnClickListener {
                addFeed(newFeed.text.toString(), spinner.selectedItem.toString())
                newFeed.setText("")
            }
        }
    }

    private fun addFeed(feedText:String, roomName:String){
        val user = firebaseAuth.currentUser
        val feed = FeedModel("0", roomName, user?.displayName!!, feedText, Timestamp.now())
        feeds.add(feed)
        feedsAdapter.setFeed(feeds)
        Log.i("Feeds", feeds.toString())
    }

//    private fun generateDummyList(size: Int): List<FeedModel> {
//        val list = ArrayList<FeedModel>()
//        val text = "Contrary to popular belief Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old."
//        for (i in 0 until size) {
//            val item = FeedModel("XXXYYYY", "Abhigyan Abhikaushalam Students Forum", "Posted by Yuvaan", text, Timestamp.now())
//            list += item
//        }
//        return list
//    }
}