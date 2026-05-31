package com.example.downsteps1

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
class ConnectGameActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var linesView: ConnectLinesView

    private lateinit var leftViews: List<TextView>
    private lateinit var rightViews: List<TextView>

    private var selectedLeft: TextView? = null
    private var selectedKey: String? = null
    private var score = 0
    private var level = 1
    private var correctCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_game)

        tvScore = findViewById(R.id.tvScore)
        linesView = findViewById(R.id.linesView)

        leftViews = listOf(
            findViewById(R.id.left1),
            findViewById(R.id.left2),
            findViewById(R.id.left3),
            findViewById(R.id.left4),
            findViewById(R.id.left5),
            findViewById(R.id.left6)
        )

        rightViews = listOf(
            findViewById(R.id.right1),
            findViewById(R.id.right2),
            findViewById(R.id.right3),
            findViewById(R.id.right4),
            findViewById(R.id.right5),
            findViewById(R.id.right6)
        )
        startLevel()
        findViewById<ImageView>(R.id.btnBack)
            .setOnClickListener {
                finish()
            }
    }

    private fun startLevel() {
        correctCount = 0
        selectedLeft = null
        selectedKey = null
        linesView.clearLines()

        tvScore.text = "Level $level | Score: $score"

        val allItems = listOf(

            Pair("🍉", "watermelon"),
            Pair("🎁", "gift"),
            Pair("🏀", "ball"),
            Pair("🟣", "circle"),
            Pair("⭐", "star"),
            Pair("🍎", "apple"),
            Pair("🚗", "car"),
            Pair("🐶", "dog"),
            Pair("🐱", "cat"),
            Pair("🌸", "flower"),

            Pair("🍌", "banana"),
            Pair("🍓", "strawberry"),
            Pair("🍒", "cherry"),
            Pair("🍇", "grapes"),
            Pair("🍍", "pineapple"),
            Pair("🥕", "carrot"),
            Pair("🌽", "corn"),
            Pair("🍪", "cookie"),
            Pair("🍰", "cake"),
            Pair("🍭", "candy"),

            Pair("🐰", "rabbit"),
            Pair("🐻", "bear"),
            Pair("🦁", "lion"),
            Pair("🐼", "panda"),
            Pair("🐸", "frog"),
            Pair("🐵", "monkey"),
            Pair("🐔", "chicken"),
            Pair("🐮", "cow"),
            Pair("🐟", "fish"),
            Pair("🦋", "butterfly"),

            Pair("🚀", "rocket"),
            Pair("✈️", "plane"),
            Pair("🚲", "bike"),
            Pair("🚂", "train"),
            Pair("⛵", "boat"),
            Pair("🏠", "house"),
            Pair("📚", "books"),
            Pair("✏️", "pencil"),
            Pair("🎨", "paint"),
            Pair("🎵", "music"),

            Pair("🔴", "red"),
            Pair("🔵", "blue"),
            Pair("🟢", "green"),
            Pair("🟡", "yellow"),
            Pair("🟠", "orange"),
            Pair("🟣", "purple"),
            Pair("⚫", "black"),
            Pair("⚪", "white"),

            Pair("🚌", "bus"),
            Pair("🚁", "helicopter"),
            Pair("🐙", "octopus"),
            Pair("🧸", "teddy"),
            Pair("⚽", "football"),
            Pair("🎈", "balloon"),
            Pair("☀️", "sun"),
            Pair("🌙", "moon"),
            Pair("🌈", "rainbow"),
            Pair("❄️", "snow")
        )

        val pairsCount = when {
            level <= 2 -> 2      // 4 cards
            level <= 4 -> 3      // 6 cards
            level <= 6 -> 4      // 8 cards
            level <= 8 -> 5      // 10 cards
            else -> 6            // 12 cards
        }

        val selectedItems = allItems.shuffled().take(pairsCount)
        val leftItems = selectedItems.shuffled()
        val rightItems = selectedItems.shuffled()

        leftViews.forEachIndexed { index, view ->
            if (index < pairsCount) {
                view.visibility = android.view.View.VISIBLE
                view.text = leftItems[index].first
                view.tag = leftItems[index].second
                view.isEnabled = true
                view.alpha = 1f

                view.setOnClickListener {
                    selectedLeft = view
                    selectedKey = view.tag.toString()
                    view.alpha = 0.6f
                }
            } else {
                view.visibility = android.view.View.GONE
            }
        }

        rightViews.forEachIndexed { index, view ->
            if (index < pairsCount) {
                view.visibility = android.view.View.VISIBLE
                view.text = rightItems[index].first
                view.tag = rightItems[index].second
                view.isEnabled = true
                view.alpha = 1f

                view.setOnClickListener {
                    checkAnswer(view, pairsCount)
                }
            } else {
                view.visibility = android.view.View.GONE
            }
        }
    }

    private fun checkAnswer(rightView: TextView, pairsCount: Int) {
        val left = selectedLeft ?: return
        val key = selectedKey ?: return

        if (key == rightView.tag.toString()) {
            score += 10
            correctCount++
            tvScore.text = "Level $level | Score: $score"

            left.isEnabled = false
            rightView.isEnabled = false
            left.alpha = 1f
            rightView.alpha = 1f

            drawLineBetween(left, rightView)

            selectedLeft = null
            selectedKey = null

            if (correctCount == pairsCount) {
                if (level < 30) {
                    level++
                    tvScore.postDelayed({
                        startLevel()
                    }, 1000)
                } else {
                    tvScore.text = "Completed all 30 levels 🎉"
                }
            }

        } else {
            left.alpha = 1f
            selectedLeft = null
            selectedKey = null
        }
    }

    private fun drawLineBetween(
        left: TextView,
        right: TextView
    ) {

        val leftLocation = IntArray(2)
        val rightLocation = IntArray(2)
        val parentLocation = IntArray(2)

        left.getLocationOnScreen(leftLocation)
        right.getLocationOnScreen(rightLocation)
        linesView.getLocationOnScreen(parentLocation)

        val startX =
            leftLocation[0] +
                    left.width -
                    parentLocation[0].toFloat()

        val startY =
            leftLocation[1] +
                    left.height / 2f -
                    parentLocation[1].toFloat()

        val endX =
            rightLocation[0] -
                    parentLocation[0].toFloat()

        val endY =
            rightLocation[1] +
                    right.height / 2f -
                    parentLocation[1].toFloat()

        val colors = listOf(
            Color.parseColor("#4CAF50"),
            Color.parseColor("#FF9800"),
            Color.parseColor("#2196F3"),
            Color.parseColor("#E91E63")
        )

        linesView.addLine(
            startX,
            startY,
            endX,
            endY,
            colors.random()
        )
    }
}