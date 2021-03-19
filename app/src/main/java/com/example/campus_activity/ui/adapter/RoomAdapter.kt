package com.example.campus_activity.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.ui.chat.ChatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.math.log

@ActivityScoped
class RoomAdapter
@Inject
constructor(
    @ActivityContext
    val context: Context,
    firebaseAuth: FirebaseAuth
) : RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    private val mInflater = LayoutInflater.from(context)
    private var rooms:List<RoomModel> = ArrayList()
    private val user = firebaseAuth.currentUser

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = mInflater.inflate(R.layout.card_room, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your data set at this position and replace the
        // contents of the view with that element
        val currentItem = rooms[position]

        viewHolder.roomTextView.text = currentItem.name

        Glide.with(context)
            .load(currentItem.uri)
            .placeholder(R.drawable.iiitm)
            .into(viewHolder.imageView)

//        viewHolder.lastMessage.text = currentItem.lastMessage

//        val timestamp = currentItem.timestamp?.toDate().toString()

//        viewHolder.timeTextView.text = timestamp.substring(11, 16)
    }

    fun setFeed(roomModels: List<RoomModel>){
        rooms = roomModels
    }

    // Return the size
    override fun getItemCount() = rooms.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val roomTextView : TextView = view.findViewById(R.id.room_name_text_view)
//        val imageCard:CardView = view.findViewById(R.id.logo_card)
        val imageView:ImageView = view.findViewById(R.id.logo)
//        val defaultImage:ImageView = view.findViewById(R.id.logo_default)
//        val lastMessage : TextView = view.findViewById(R.id.last_msg_text_view)
//        val timeTextView : TextView = view.findViewById(R.id.time_text_view)

        init {
            view.setOnClickListener(this)
//            if(rooms[adapterPosition].uri == null){
//                defaultImage.visibility = View.VISIBLE
//                imageCard.visibility = View.GONE
//                imageView.visibility = View.GONE
//            }
//            else{
//                defaultImage.visibility = View.GONE
//                imageCard.visibility = View.VISIBLE
//                imageView.visibility = View.VISIBLE
//            }
            try {
                Log.i("View", rooms[adapterPosition].toString())
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        override fun onClick(v: View?) {
            val intent = Intent(v?.context, ChatActivity::class.java)
            intent.putExtra("room", rooms[adapterPosition])
            v?.context?.startActivity(intent)
        }
    }

}
