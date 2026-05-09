package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.GameModel
import com.example.downsteps1.ui.adapter.GamesAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Calendar

class GamesActivity : BaseActivity() {

    private lateinit var recyclerGames: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter

    private lateinit var gamesList: MutableList<GameModel>

    private lateinit var imgSuggestedGame: ImageView
    private lateinit var tvSuggestedName: TextView
    private lateinit var tvSuggestedDesc: TextView
    private lateinit var btnSuggestedStart: MaterialButton

    private lateinit var btnFavorites: MaterialCardView
    private lateinit var iconFav: ImageView
    private lateinit var tvFavorites: TextView
    private lateinit var layoutEmpty: LinearLayout

    private var showFavoritesOnly = false

    private val PREFS_NAME = "favorites_prefs"
    private val KEY_FAVORITES = "favorites_list"

    private lateinit var suggestedGame: GameModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        recyclerGames = findViewById(R.id.recyclerGames)
        imgSuggestedGame = findViewById(R.id.imgSuggestedGame)
        tvSuggestedName = findViewById(R.id.tvSuggestedName)
        tvSuggestedDesc = findViewById(R.id.tvSuggestedDesc)
        btnSuggestedStart = findViewById(R.id.btnSuggestedStart)

        btnFavorites = findViewById(R.id.btnFavorites)
        iconFav = findViewById(R.id.iconFav)
        tvFavorites = findViewById(R.id.tvFavorites)
        layoutEmpty = findViewById(R.id.layoutEmpty)

        findViewById<ImageView>(R.id.btnBackChallange1).setOnClickListener {
            finish()
        }

        gamesList = mutableListOf(
            GameModel(
                name = "Connect",
                description = "Match similar items",
                suggestedBenefit = "Helps improve focus by connecting similar elements in a fun way.",
                level = "Level 1",
                imageResId = R.drawable.ic_connect,
                backgroundColor = "#EEF4FF",
                buttonColor = "#4C6FFF",
                levelBgColor = "#E3EDFF",
                levelTextColor = "#4C6FFF"
            ),
            GameModel(
                name = "Learn",
                description = "Learn letters and words",
                suggestedBenefit = "Helps children learn letters and words easily.",
                level = "Level 1",
                imageResId = R.drawable.ic_learn,
                backgroundColor = "#F5EEFF",
                buttonColor = "#A66BFF",
                levelBgColor = "#EEE6FF",
                levelTextColor = "#A66BFF",
                isPopular = true
            ),
            GameModel(
                name = "Matching",
                description = "Match similar images",
                suggestedBenefit = "Improves memory and concentration.",
                level = "Level 2",
                imageResId = R.drawable.ic_matching,
                backgroundColor = "#FFF4E8",
                buttonColor = "#FF9F2E",
                levelBgColor = "#FFE9D2",
                levelTextColor = "#FF9F2E"
            ),
            GameModel(
                name = "Puzzle",
                description = "Build the image from pieces",
                suggestedBenefit = "Enhances thinking and problem-solving.",
                level = "Level 2",
                imageResId = R.drawable.ic_puzzle,
                backgroundColor = "#EEF9EE",
                buttonColor = "#59C36A",
                levelBgColor = "#E4F6E8",
                levelTextColor = "#59C36A"
            ),
            GameModel(
                name = "Coloring",
                description = "Color beautiful drawings",
                suggestedBenefit = "Improves color recognition and motor skills.",
                level = "Level 1",
                imageResId = R.drawable.ic_color_palette,
                backgroundColor = "#FFEFF4",
                buttonColor = "#FF6FA0",
                levelBgColor = "#FFE3EC",
                levelTextColor = "#FF6FA0"
            )
        )

        loadFavorites()

        gamesAdapter = GamesAdapter(
            gamesList = gamesList.toMutableList(),
            onFavoriteChanged = {
                saveFavorites()
                if (showFavoritesOnly) updateFavoritesList()
            },
            onGameClick = { game ->
                openGame(game)
            }
        )

        recyclerGames.layoutManager = GridLayoutManager(this, 2)
        recyclerGames.adapter = gamesAdapter

        updateSuggestedGame()
        setupFavoritesButton()

        btnSuggestedStart.setOnClickListener {
            openGame(suggestedGame)
        }
    }

    private fun updateSuggestedGame() {
        val index = Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % gamesList.size
        suggestedGame = gamesList[index]

        imgSuggestedGame.setImageResource(suggestedGame.imageResId)
        tvSuggestedName.text = suggestedGame.name
        tvSuggestedDesc.text = suggestedGame.suggestedBenefit
    }

    private fun setupFavoritesButton() {
        btnFavorites.setOnClickListener {
            showFavoritesOnly = !showFavoritesOnly
            updateFavoritesList()
            updateFavoriteButtonUI()
        }
    }

    private fun updateFavoritesList() {
        val filtered = if (showFavoritesOnly)
            gamesList.filter { it.isFavorite }.toMutableList()
        else
            gamesList.toMutableList()

        gamesAdapter.updateList(filtered)

        layoutEmpty.visibility =
            if (showFavoritesOnly && filtered.isEmpty()) View.VISIBLE else View.GONE
        recyclerGames.visibility =
            if (layoutEmpty.visibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    private fun updateFavoriteButtonUI() {
        if (showFavoritesOnly) {
            btnFavorites.setCardBackgroundColor(Color.parseColor("#FFEFF4"))
            iconFav.setImageResource(R.drawable.ic_heart_filled)
            iconFav.setColorFilter(Color.parseColor("#FF6FA0"))
            tvFavorites.setTextColor(Color.parseColor("#FF6FA0"))
        } else {
            btnFavorites.setCardBackgroundColor(Color.WHITE)
            iconFav.setImageResource(R.drawable.ic_heart_outline)
            iconFav.setColorFilter(Color.parseColor("#25336E"))
            tvFavorites.setTextColor(Color.parseColor("#25336E"))
        }
    }

    private fun saveFavorites() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val favoriteNames = gamesList.filter { it.isFavorite }.map { it.name }.toSet()
        prefs.edit().putStringSet(KEY_FAVORITES, favoriteNames).apply()
    }

    private fun loadFavorites() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val saved = prefs.getStringSet(KEY_FAVORITES, emptySet())

        gamesList.forEach {
            it.isFavorite = saved?.contains(it.name) == true
        }
    }

    private fun openGame(game: GameModel) {
        Toast.makeText(this, "${game.name} is not implemented yet", Toast.LENGTH_SHORT).show()
    }
}