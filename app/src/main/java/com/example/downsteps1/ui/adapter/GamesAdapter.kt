package com.example.downsteps1.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.GameModel
import com.google.android.material.card.MaterialCardView

class GamesAdapter(
    private var gamesList: MutableList<GameModel>,
    private val onFavoriteChanged: () -> Unit,
    private val onGameClick: (GameModel) -> Unit
) : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rootCard: MaterialCardView = itemView as MaterialCardView
        val imgGame: ImageView = itemView.findViewById(R.id.imgGame)
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)
        val tvGameDesc: TextView = itemView.findViewById(R.id.tvGameDesc)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]

        holder.imgGame.setImageResource(game.imageResId)
        holder.tvGameName.text = game.name
        holder.tvGameDesc.text = game.description

            if (game.isPopular) View.VISIBLE else View.GONE

        holder.rootCard.setCardBackgroundColor(
            Color.parseColor(game.backgroundColor)
        )

        holder.rootCard.setOnClickListener {
            onGameClick(game)
        }
    }

    override fun getItemCount(): Int = gamesList.size

    fun updateList(newList: MutableList<GameModel>) {
        gamesList = newList
        notifyDataSetChanged()
    }
}