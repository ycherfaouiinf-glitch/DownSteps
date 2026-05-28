package com.example.downsteps1.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.FaqItem

class FaqAdapter(
    private var items: List<FaqItem>
) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {

    inner class FaqViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val header: LinearLayout =
            itemView.findViewById(R.id.headerFaq)

        val tvQuestion: TextView =
            itemView.findViewById(R.id.tvFaqQuestion)

        val tvAnswer: TextView =
            itemView.findViewById(R.id.tvFaqAnswer)

        val iconArrow: ImageView =
            itemView.findViewById(R.id.iconFaqArrow)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FaqViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_faq, parent, false)

        return FaqViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FaqViewHolder,
        position: Int
    ) {

        val item = items[position]

        holder.tvQuestion.text = item.question
        holder.tvAnswer.text = item.answer

        holder.tvAnswer.visibility =
            if (item.isExpanded) View.VISIBLE
            else View.GONE

        holder.iconArrow.rotation =
            if (item.isExpanded) 180f
            else 0f

        holder.header.setOnClickListener {

            item.isExpanded = !item.isExpanded

            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<FaqItem>) {
        items = newItems
        notifyDataSetChanged()
    }
}