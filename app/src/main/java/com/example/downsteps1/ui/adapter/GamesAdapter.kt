package com.example.downsteps1.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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
        val tvGameLevel: TextView = itemView.findViewById(R.id.tvGameLevel)
        val btnFavorite: ImageView = itemView.findViewById(R.id.btnFavorite)
        val imgGame: ImageView = itemView.findViewById(R.id.imgGame)
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)
        val tvGameDesc: TextView = itemView.findViewById(R.id.tvGameDesc)
        val btnStartCircle: MaterialCardView = itemView.findViewById(R.id.btnStartCircle)
        val badgePopular: View = itemView.findViewById(R.id.badgePopular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gamesList[position]

        holder.tvGameLevel.text = game.level
        holder.imgGame.setImageResource(game.imageResId)
        holder.tvGameName.text = game.name
        holder.tvGameDesc.text = game.description
        holder.badgePopular.visibility = if (game.isPopular) View.VISIBLE else View.GONE

        holder.rootCard.setCardBackgroundColor(Color.parseColor(game.backgroundColor))
        holder.btnStartCircle.setCardBackgroundColor(Color.parseColor(game.buttonColor))

        val levelDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 50f
            setColor(Color.parseColor(game.levelBgColor))
        }
        holder.tvGameLevel.background = levelDrawable
        holder.tvGameLevel.setTextColor(Color.parseColor(game.levelTextColor))

        if (game.isFavorite) {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        } else {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_outline)
        }

        holder.btnFavorite.setOnClickListener {
            game.isFavorite = !game.isFavorite

            holder.btnFavorite.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction {
                    holder.btnFavorite.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()

            notifyItemChanged(position)
            onFavoriteChanged()
        }

        holder.rootCard.setOnClickListener {
            onGameClick(game)
        }

        holder.btnStartCircle.setOnClickListener {
            onGameClick(game)
        }
    }

    override fun getItemCount(): Int = gamesList.size

    fun updateList(newList: MutableList<GameModel>) {
        gamesList = newList
        notifyDataSetChanged()
    }
}