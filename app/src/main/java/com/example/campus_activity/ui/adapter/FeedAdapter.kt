package com.example.campus_activity.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campus_activity.R
import com.example.campus_activity.data.model.FeedModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FeedAdapter
@Inject
constructor(
    @ActivityContext
    context: Context
) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    private val mInflater = LayoutInflater.from(context)
    private var feeds:List<FeedModel> = ArrayList<FeedModel>()

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = mInflater.inflate(R.layout.card_feed, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your data set at this position and replace the
        // contents of the view with that element
        val currentItem = feeds[position]

        viewHolder.senderTextView.text = currentItem.sender_name
        viewHolder.postTextView.text = currentItem.post
        viewHolder.timeTextView.text = currentItem.time
    }

    fun setFeed(feedModel: List<FeedModel>){
        feeds = feedModel
    }

    // Return the size
    override fun getItemCount() = feeds.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val senderTextView : TextView = view.findViewById(R.id.sender_text_view)
        val postTextView : TextView = view.findViewById(R.id.des_text_view)
        val timeTextView : TextView = view.findViewById(R.id.time_text_view)
    }

}
