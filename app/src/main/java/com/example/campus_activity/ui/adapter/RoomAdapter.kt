package com.example.campus_activity.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import com.example.campus_activity.data.model.RoomModel
import com.example.campus_activity.ui.chat.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RoomAdapter
@Inject
constructor(
    @ActivityContext
    context: Context,
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
//        val lastMessage : TextView = view.findViewById(R.id.last_msg_text_view)
//        val timeTextView : TextView = view.findViewById(R.id.time_text_view)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val intent = Intent(v?.context, ChatActivity::class.java)
            intent.putExtra("room", rooms[adapterPosition])
            v?.context?.startActivity(intent)
        }
    }

}
