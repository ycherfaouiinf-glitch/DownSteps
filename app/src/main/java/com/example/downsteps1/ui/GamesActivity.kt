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
import android.content.Intent
import com.example.downsteps1.MatchingGameActivity
import com.example.downsteps1.ConnectGameActivity
import com.example.downsteps1.ui.HomeActivity

class GamesActivity : BaseActivity() {

    private lateinit var recyclerGames: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter

    private lateinit var gamesList: MutableList<GameModel>

    private lateinit var imgSuggestedGame: ImageView
    private lateinit var tvSuggestedName: TextView
    private lateinit var tvSuggestedDesc: TextView
    private lateinit var btnSuggestedStart: MaterialButton
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


        layoutEmpty = findViewById(R.id.layoutEmpty)

        findViewById<ImageView>(R.id.btnBackChallange1).setOnClickListener {

            startActivity(
                Intent(this, HomeActivity::class.java)
            )

            finish()
        }

        gamesList = mutableListOf(
            GameModel(
                name = getString(R.string.game_connect),
                description = getString(R.string.game_connect_desc),
                suggestedBenefit = getString(R.string.game_connect_benefit),
                level = getString(R.string.level_1),
                imageResId = R.drawable.ic_connect,
                backgroundColor =
                    String.format(
                        "#%06X",
                        0xFFFFFF and getColor(R.color.game_connect_bg)
                    ),
                buttonColor = "#4C6FFF",
                levelBgColor = "#E3EDFF",
                levelTextColor = "#4C6FFF"
            ),
            GameModel(
                name = getString(R.string.game_matching),
                description = getString(R.string.game_matching_desc),
                suggestedBenefit = getString(R.string.game_matching_benefit),
                level = getString(R.string.level_2),
                imageResId = R.drawable.ic_matching,
                backgroundColor =
                    String.format(
                        "#%06X",
                        0xFFFFFF and getColor(R.color.game_matching_bg)
                    ),
                buttonColor = "#FF9F2E",
                levelBgColor = "#FFE9D2",
                levelTextColor = "#FF9F2E"
            ),
            GameModel(
                name = getString(R.string.game_coloring),
                description = getString(R.string.game_coloring_desc),
                suggestedBenefit = getString(R.string.game_coloring_benefit),
                level = getString(R.string.level_1),
                imageResId = R.drawable.ic_color_palette,
                backgroundColor =
                    String.format(
                        "#%06X",
                        0xFFFFFF and getColor(R.color.game_coloring_bg)
                    ),
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

        when (game.name) {

            getString(R.string.game_connect) -> {
                startActivity(
                    Intent(this, ConnectGameActivity::class.java)
                )
            }

            getString(R.string.game_matching) -> {
                startActivity(
                    Intent(this, MatchingGameActivity::class.java)
                )
            }

            else -> {
                Toast.makeText(
                    this,
                    getString(
                        R.string.game_not_implemented,
                        game.name
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}