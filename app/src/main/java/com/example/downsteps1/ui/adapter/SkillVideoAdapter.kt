package com.example.downsteps1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.SkillVideo
import com.google.android.material.card.MaterialCardView

class SkillVideoAdapter(
    private val videoList: List<SkillVideo>,
    private val onVideoClick: (SkillVideo) -> Unit
) : RecyclerView.Adapter<SkillVideoAdapter.SkillVideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillVideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_skill_video, parent, false)
        return SkillVideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillVideoViewHolder, position: Int) {
        holder.bind(videoList[position])
    }

    override fun getItemCount(): Int = videoList.size

    inner class SkillVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cardVideo: MaterialCardView = itemView.findViewById(R.id.cardVideo)
        private val txtTitle: TextView = itemView.findViewById(R.id.tvVideoTitle)
        private val txtDescription: TextView = itemView.findViewById(R.id.tvVideoDesc)
        private val imgPlay: ImageView = itemView.findViewById(R.id.imgVideo)

        fun bind(video: SkillVideo) {
            txtTitle.text = video.title
            txtDescription.text = video.description

            cardVideo.setOnClickListener {
                onVideoClick(video)
            }

            imgPlay.setOnClickListener {
                onVideoClick(video)
            }
        }
    }
}
