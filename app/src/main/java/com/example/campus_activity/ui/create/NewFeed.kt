package com.example.campus_activity.ui.create

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.Result
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.data.viewmodels.FeedsViewModel
import com.example.campus_activity.data.viewmodels.RoomsViewModel
import com.example.campus_activity.ui.handler.ImageHelper
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class NewFeed : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var imageHelper: ImageHelper

    private val feedsViewModel:FeedsViewModel by viewModels()
    private val roomsViewModel:RoomsViewModel by viewModels()

    private lateinit var addFeedCard : MaterialCardView
    private lateinit var newFeed: TextInputEditText
    private lateinit var postImage: ImageView
    private lateinit var removeImage: ImageButton
    private lateinit var addImageButton: MaterialCardView
    private lateinit var spinner: Spinner
    private lateinit var uploadButton: Button
    private var imageUri:Uri? = null
    private var rooms:ArrayList<RoomModel> = ArrayList()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_feed)

        addFeedCard = findViewById(R.id.add_feed_card)
        newFeed = findViewById(R.id.new_feed_text_view)
        postImage = findViewById(R.id.post_image_view)
        removeImage = findViewById(R.id.remove_image_button)
        addImageButton = findViewById(R.id.add_image_button)
        spinner = findViewById(R.id.list_club_spinner)
        uploadButton  = findViewById(R.id.add_feed_button)

        addImageButton.setOnClickListener {
            val intent = imageHelper.pickImage()
            startActivityForResult(intent, imageHelper.PICK_IMAGE)
        }

        removeImage.setOnClickListener {
            imageUri = null
            postImage.setImageDrawable(resources.getDrawable(R.drawable.ic_baseline_block_24, this.theme))
            removeImage.visibility = View.GONE
        }

        window.setBackgroundDrawable(resources.getDrawable(R.drawable.maskgroup, this.theme))

        setUpAddFeed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == imageHelper.PICK_IMAGE) {
            imageUri = data?.data
            try {
                val cropIntent = imageHelper.cropImage(imageUri!!, 16, 9)
                startActivityForResult(cropIntent, imageHelper.CROP_IMAGE)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        else if(resultCode == RESULT_OK && requestCode == imageHelper.CROP_IMAGE){
            try {
                imageUri = data?.data
                val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!))
                postImage.setImageBitmap(bitmap)
                removeImage.visibility = View.VISIBLE
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setUpAddFeed(){
        val user = firebaseAuth.currentUser
        if(user != null){
            roomsViewModel.getAllRooms()
            roomsViewModel.allRooms.observe(this, {
                rooms.clear()
                it.map { r ->
                    val checkString = r.members?.find {s ->
                        s == user.email
                    }
                    if(user.email!!.toString() == r.admin || checkString == user.email){
                        rooms.add(r)
                        Log.i("Room", r.name!!)
                    }
                }

                //  Check if member or admin
                if(rooms.isEmpty()){
                    addFeedCard.visibility = View.GONE
                }
                else{
                    addFeedCard.visibility = View.VISIBLE
                    val adapter = ArrayAdapter(
                        this,
                        R.layout.list_item_add_feed_spinner,
                        rooms.map { r ->
                            r.name
                        }
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                    spinner.adapter = adapter
                    spinner.setSelection(0, true)

                    uploadButton.setOnClickListener {
                        if(newFeed.text.toString() != "" && spinner.selectedItem.toString() != ""){
                            addFeed(newFeed.text.toString(), spinner.selectedItem.toString())
                            newFeed.setText("")
                        }
                    }
                }
            })
        }else{
            addFeedCard.visibility = View.GONE
        }
    }

    //  Add a new feed
    private fun addFeed(feedText:String, roomName:String){
        val user = firebaseAuth.currentUser
        val spinnerPosition = spinner.selectedItemPosition
        val feed = FeedModel(user?.displayName!!, feedText, rooms[spinnerPosition], null, Timestamp.now())
        if(imageUri == null){
            feedsViewModel.insertFeed(feed)
            finish()
        }
        else{
            feedsViewModel.uploadImage("feeds/", imageUri!!).observe(this, {
                when (it) {
                    is Result.Progress -> {
                        Toast.makeText(this, "Uploading Feed . . .", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        imageUri = it.result
                        feed.uri = imageUri.toString()
                        feedsViewModel.insertFeed(feed)
                        Toast.makeText(this, "Feed Added!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }

}