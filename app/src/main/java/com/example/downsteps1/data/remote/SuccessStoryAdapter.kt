package com.example.downsteps1.data.remote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R

class SuccessStoryAdapter(
    private val stories: List<SuccessStory>
) : RecyclerView.Adapter<SuccessStoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val headerStory: LinearLayout = itemView.findViewById(R.id.headerStory)
        val contentStory: LinearLayout = itemView.findViewById(R.id.contentStory)
        val tvTitle: TextView = itemView.findViewById(R.id.tvStoryTitle)
        val tvText: TextView = itemView.findViewById(R.id.tvStoryText)
        val tvInfo: TextView = itemView.findViewById(R.id.tvStoryInfo)
        val imgStory: ImageView = itemView.findViewById(R.id.imgStory)
        val iconArrow: ImageView = itemView.findViewById(R.id.iconArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_success_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]

        holder.tvTitle.text = story.title
        holder.tvText.text = story.description
        holder.tvInfo.text = "${story.category} • ${story.year}"
        holder.imgStory.setImageResource(story.imageRes)

        holder.headerStory.setOnClickListener {
            val isClosed = holder.contentStory.visibility == View.GONE
            holder.contentStory.visibility = if (isClosed) View.VISIBLE else View.GONE
            holder.iconArrow.rotation = if (isClosed) 180f else 0f
        }
    }

    override fun getItemCount(): Int = stories.size
}