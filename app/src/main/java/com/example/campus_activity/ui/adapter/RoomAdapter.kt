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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.card_room, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val currentItem = rooms[position]

        viewHolder.roomTextView.text = currentItem.name

        Glide.with(context)
            .load(currentItem.uri)
            .placeholder(R.drawable.iiitm)
            .into(viewHolder.imageView)
    }

    fun setRooms(roomModels: List<RoomModel>){
        rooms = roomModels
        notifyDataSetChanged()
    }

    // Return the size
    override fun getItemCount() = rooms.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val roomTextView : TextView = view.findViewById(R.id.room_name_text_view)
        val imageView:ImageView = view.findViewById(R.id.logo)

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
