package com.example.campus_activity.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_WITHOUT_IMAGE = 1
    private val VIEW_WITH_IMAGE = 2
    private val mInflater = LayoutInflater.from(context)
    private var feeds:List<FeedModel> = ArrayList<FeedModel>()

    override fun getItemViewType(position: Int): Int {
        return if(feeds[position].uri == null){
            VIEW_WITHOUT_IMAGE
        }
        else{
            VIEW_WITH_IMAGE
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view :View
        return when(viewType){
            VIEW_WITHOUT_IMAGE -> {
                view = mInflater.inflate(R.layout.card_feed, viewGroup, false)
                WithoutImageViewHolder(view)
            }
            else -> {
                view = mInflater.inflate(R.layout.card_feed_image, viewGroup, false)
                WithImageViewHolder(view)
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = feeds[position]
        when(viewHolder.itemViewType){
            VIEW_WITHOUT_IMAGE -> {
                (viewHolder as WithoutImageViewHolder).bind(currentItem)
            }
            else -> {
                (viewHolder as WithImageViewHolder).bind(currentItem)
            }
        }
    }

    fun setFeed(feedModel: List<FeedModel>){
        feeds = feedModel
        notifyDataSetChanged()
    }

    fun addFeed(position: Int){
        notifyItemInserted(position)
    }

    // Return the size
    override fun getItemCount() = feeds.size

    inner class WithoutImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val senderTextView : TextView = view.findViewById(R.id.sender_text_view)
        private val posterByTextView : TextView = view.findViewById(R.id.des_text_view)
        private val postTextView:TextView = view.findViewById(R.id.feed_content_text_view)
        private val timeTextView : TextView = view.findViewById(R.id.time_text_view)
        private val dateTextView : TextView = view.findViewById(R.id.date_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(feedModel: FeedModel){
            senderTextView.text = feedModel.sender_name
            posterByTextView.text = "Posted by ${feedModel.posted_by}"
            postTextView.text = feedModel.post

            val timestamp = feedModel.timestamp.toDate().toString()
            val date = timestamp.substring(4, 10) + ", " + timestamp.substring(timestamp.length - 4, timestamp.length)

            timeTextView.text = timestamp.substring(11, 16)
            dateTextView.text = date
        }
    }

    inner class WithImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val postImageView: ImageView = view.findViewById(R.id.post_image_view)
        private val senderTextView : TextView = view.findViewById(R.id.sender_text_view)
        private val posterByTextView : TextView = view.findViewById(R.id.des_text_view)
        private val postTextView:TextView = view.findViewById(R.id.feed_content_text_view)
        private val timeTextView : TextView = view.findViewById(R.id.time_text_view)
        private val dateTextView : TextView = view.findViewById(R.id.date_text_view)

        @SuppressLint("SetTextI18n")
        fun bind(feedModel: FeedModel){
            senderTextView.text = feedModel.sender_name
            posterByTextView.text = "Posted by ${feedModel.posted_by}"
            postTextView.text = feedModel.post

            Glide.with(context)
                .load(feedModel.uri)
                .into(postImageView)

            val timestamp = feedModel.timestamp.toDate().toString()
            val date = timestamp.substring(4, 10) + ", " + timestamp.substring(timestamp.length - 4, timestamp.length)

            timeTextView.text = timestamp.substring(11, 16)
            dateTextView.text = date
        }
    }

}
