package com.example.downsteps1.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.ui.model.CenterModel

class CentersAdapter(
    private var centersList: List<CenterModel>
) : RecyclerView.Adapter<CentersAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCenter: ImageView = itemView.findViewById(R.id.imgCenter)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        val btnCall: MaterialButton = itemView.findViewById(R.id.btnCall)
        val btnLocation: MaterialButton = itemView.findViewById(R.id.btnLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_center, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centersList[position]

        holder.tvName.text = center.name
        holder.tvLocation.text = center.location
        holder.tvPhone.text = center.phone
        holder.tvCategory.text = center.category
        holder.imgCenter.setImageResource(center.imageRes)

        holder.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${center.phone}")
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.btnLocation.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=${Uri.encode(center.mapQuery)}")
            )
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = centersList.size

    fun updateList(newList: List<CenterModel>) {
        centersList = newList
        notifyDataSetChanged()
    }
}