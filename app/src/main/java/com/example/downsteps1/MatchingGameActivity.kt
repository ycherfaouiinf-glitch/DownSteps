package com.example.downsteps1

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.GridLayout
class MatchingGameActivity : AppCompatActivity() {

    private lateinit var cardsViews: List<TextView>
    private lateinit var cardsValues: List<String>
    private lateinit var tvScore: TextView
    private lateinit var levelMessageBox: LinearLayout
    private lateinit var tvLevelMessage: TextView
    private lateinit var btnNextLevel: Button
    private lateinit var gridCards: GridLayout
    private var level = 1
    private var score = 0
    private var matchedPairs = 0
    private var firstIndex: Int? = null
    private var secondIndex: Int? = null
    private var isBusy = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matching_game)

        tvScore = findViewById(R.id.tvScore)
        levelMessageBox = findViewById(R.id.levelMessageBox)
        tvLevelMessage = findViewById(R.id.tvLevelMessage)
        btnNextLevel = findViewById(R.id.btnNextLevel)
        gridCards = findViewById(R.id.gridCards)
        btnNextLevel.setOnClickListener {
            levelMessageBox.visibility = android.view.View.GONE
            gridCards.visibility = android.view.View.VISIBLE
            startLevel(level + 1)
        }
        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        cardsViews = listOf(
            findViewById(R.id.card1),
            findViewById(R.id.card2),
            findViewById(R.id.card3),
            findViewById(R.id.card4),
            findViewById(R.id.card5),
            findViewById(R.id.card6),
            findViewById(R.id.card7),
            findViewById(R.id.card8),
            findViewById(R.id.card9),
            findViewById(R.id.card10),
            findViewById(R.id.card11),
            findViewById(R.id.card12)
        )

        startLevel(1)
    }

    private fun startLevel(newLevel: Int) {
        level = newLevel

        val symbols = when (level) {
            1 -> listOf("🐔", "🐮", "🐟")
            2 -> listOf("🐔", "🐮", "🐟", "🌸")
            else -> listOf("🐔", "🐮", "🐟", "🌸", "🐉", "🐶")
        }

        val numberOfCards = symbols.size * 2
        cardsValues = (symbols + symbols).shuffled()

        matchedPairs = 0
        firstIndex = null
        secondIndex = null
        isBusy = false

        tvScore.text = "Level $level  |  Score: $score"

        cardsViews.forEachIndexed { index, card ->
            if (index < numberOfCards) {
                card.visibility = TextView.VISIBLE
                card.text = ""
                card.isEnabled = true
                card.setBackgroundResource(R.drawable.bg_card_back)

                card.setOnClickListener {
                    onCardClicked(index)
                }
            } else {
                card.visibility = TextView.GONE
            }
        }
    }

    private fun onCardClicked(index: Int) {
        if (isBusy) return
        if (!cardsViews[index].isEnabled) return
        if (firstIndex == index) return

        openCard(index)

        if (firstIndex == null) {
            firstIndex = index
        } else {
            secondIndex = index
            checkCards()
        }
    }

    private fun openCard(index: Int) {
        cardsViews[index].text = cardsValues[index]
        cardsViews[index].setBackgroundResource(R.drawable.bg_card_front)
    }

    private fun closeCard(index: Int) {
        cardsViews[index].text = ""
        cardsViews[index].setBackgroundResource(R.drawable.bg_card_back)
    }

    private fun checkCards() {
        val first = firstIndex ?: return
        val second = secondIndex ?: return

        if (cardsValues[first] == cardsValues[second]) {
            cardsViews[first].isEnabled = false
            cardsViews[second].isEnabled = false

            score += 10
            matchedPairs++

            tvScore.text = "Level $level  |  Score: $score"

            firstIndex = null
            secondIndex = null

            if (matchedPairs == cardsValues.size / 2) {
                goToNextLevel()
            }

        } else {
            isBusy = true

            Handler(Looper.getMainLooper()).postDelayed({
                closeCard(first)
                closeCard(second)

                firstIndex = null
                secondIndex = null
                isBusy = false
            }, 800)
        }
    }

    private fun goToNextLevel() {
        gridCards.visibility = android.view.View.GONE
        levelMessageBox.visibility = android.view.View.VISIBLE

        if (level < 3) {
            tvLevelMessage.text = "Level $level completed successfully!"
            btnNextLevel.text = "Go to Level ${level + 1}"
        } else {
            tvLevelMessage.text = "Excellent! You completed all levels!"
            btnNextLevel.text = "Finish"
            btnNextLevel.setOnClickListener {
                finish()
            }
        }
    }
}