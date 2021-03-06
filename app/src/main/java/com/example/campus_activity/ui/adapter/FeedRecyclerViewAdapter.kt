package com.example.campus_activity.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedList


class FeedRecyclerViewAdapter(private val list : List<FeedList>) :
    RecyclerView.Adapter<FeedRecyclerViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView : TextView = view.findViewById(R.id.sender_text_view)
        val postTextView : TextView = view.findViewById(R.id.des_text_view)
        val timeTextView : TextView = view.findViewById(R.id.time_text_view)

    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.feed_card, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val currentItem = list[position]

        viewHolder.senderTextView.text = currentItem.sender_name
        viewHolder.postTextView.text = currentItem.post
        viewHolder.timeTextView.text = currentItem.time
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.size


}
