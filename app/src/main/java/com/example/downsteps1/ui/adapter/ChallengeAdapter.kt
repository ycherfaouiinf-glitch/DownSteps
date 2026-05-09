package com.example.downsteps1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.ChallengeItem

class ChallengeAdapter(
    private val challenges: List<ChallengeItem>,
    private val challengeType: String,
    private val onChallengeClick: (ChallengeItem) -> Unit
) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val circle: FrameLayout = view.findViewById(R.id.circle)
        val txtNumber: TextView = view.findViewById(R.id.txtNumber)
        val iconLock: ImageView = view.findViewById(R.id.iconLock)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtDesc: TextView = view.findViewById(R.id.txtDesc)
        val line: View = view.findViewById(R.id.line)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_challenge, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = challenges.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challenges[position]

        holder.txtTitle.text = challenge.title
        holder.txtDesc.text = challenge.description
        holder.txtNumber.text = challenge.id.toString()

        if (challenge.isLocked) {
            holder.txtNumber.visibility = View.GONE
            holder.iconLock.visibility = View.VISIBLE
            holder.circle.setBackgroundResource(R.drawable.bg_timeline_locked_circle)
            holder.itemView.alpha = 0.85f
        } else {
            holder.txtNumber.visibility = View.VISIBLE
            holder.iconLock.visibility = View.GONE
            holder.circle.setBackgroundResource(R.drawable.bg_timeline_unlocked_circle)
            holder.itemView.alpha = 1f
        }

        if (position == challenges.lastIndex) {
            holder.line.visibility = View.GONE
        } else {
            holder.line.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            if (!challenge.isLocked) {
                onChallengeClick(challenge)
            }
        }
    }
}