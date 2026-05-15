package com.example.downsteps1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.AudioItem

class AudioAdapter(
    private var audioList: List<AudioItem>,
    private val onItemClick: (AudioItem) -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.bind(audioList[position])
    }

    override fun getItemCount(): Int = audioList.size

    fun updateList(newList: List<AudioItem>) {
        audioList = newList
        notifyDataSetChanged()
    }

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imgAudioCover: ImageView = itemView.findViewById(R.id.imgAudioCover)
        private val txtAudioTitle: TextView = itemView.findViewById(R.id.txtAudioTitle)
        private val txtDuration: TextView = itemView.findViewById(R.id.txtDuration)
        private val btnQuickPlay: ImageView = itemView.findViewById(R.id.btnQuickPlay)

        fun bind(item: AudioItem) {

            txtAudioTitle.text = item.title

            txtDuration.text =
                if (item.duration.isNotBlank()) {
                    item.duration
                } else {
                    "0:00"
                }

            if (item.imageResId != 0) {
                imgAudioCover.setImageResource(item.imageResId)
            } else {
                imgAudioCover.setImageResource(android.R.drawable.ic_media_play)
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }

            btnQuickPlay.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}