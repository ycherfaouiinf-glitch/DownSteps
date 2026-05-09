package com.example.downsteps1.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.model.ChallengeItem
import com.example.downsteps1.ui.adapter.ChallengeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChallengesTimelineActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var txtType: TextView
    private lateinit var recyclerChallenges: RecyclerView

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var challengeType: String = "motor"
    private var currentDay: Int = 1
    private var challengeLevel: String = "Beginner"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_challenges_timeline)

        BottomNavHelper.setup(this, "home")

        initializeViews()
        setupClickListeners()

        challengeType = intent.getStringExtra("challenge_type") ?: "motor"
        txtType.text = getChallengeTypeLabel(challengeType)

    }
    override fun onResume() {
        super.onResume()
        loadUserProgressFromFirebase()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        txtType = findViewById(R.id.txtType)
        recyclerChallenges = findViewById(R.id.recyclerChallenges)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadUserProgressFromFirebase() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->

                if (!document.exists()) {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    showTimeline()
                    return@addOnSuccessListener
                }

                currentDay = when (challengeType) {
                    "motor" -> document.getLong("currentDayMotor")?.toInt() ?: 1
                    "language" -> document.getLong("currentDayLanguage")?.toInt() ?: 1
                    "speech" -> document.getLong("currentDaySpeech")?.toInt() ?: 1
                    else -> 1
                }

                challengeLevel = when (challengeType) {
                    "motor" -> document.getString("motorAssessment.motorLevel") ?: "Beginner"
                    "language" -> document.getString("languageAssessment.languageLevel") ?: "Beginner"
                    "speech" -> document.getString("speechAssessment.level") ?: "Beginner"
                    else -> "Beginner"
                }

                challengeLevel = normalizeLevel(challengeLevel)

                showTimeline()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                showTimeline()
            }
    }

    private fun showTimeline() {
        val challenges = mutableListOf<ChallengeItem>()

        for (day in 1..30) {
            val isLocked = day > currentDay
            val description = when {
                day < currentDay -> "Completed"
                day == currentDay -> "Open"
                else -> "Locked"
            }

            challenges.add(
                ChallengeItem(
                    id = day,
                    title = "Day $day",
                    description = description,
                    challengeType = challengeType,
                    isLocked = isLocked
                )
            )
        }

        recyclerChallenges.layoutManager = LinearLayoutManager(this)
        recyclerChallenges.setHasFixedSize(true)
        recyclerChallenges.adapter = ChallengeAdapter(
            challenges = challenges,
            challengeType = challengeType,
            onChallengeClick = { challenge ->
                openChallengeDay(
                    challengeType = challenge.challengeType,
                    day = challenge.id,
                    isCompleted = challenge.id < currentDay
                )
            }
        )
    }

    private fun openChallengeDay(
        challengeType: String,
        day: Int,
        isCompleted: Boolean
    ) {
        val intent = Intent(this, Challange1Activity::class.java).apply {
            putExtra("challenge_type", challengeType)
            putExtra("challenge_day", day)
            putExtra("challenge_level", challengeLevel)
            putExtra("is_completed", isCompleted)
        }
        startActivity(intent)
    }

    private fun getChallengeTypeLabel(type: String): String {
        return when (type) {
            "motor" -> "Motor Challenge"
            "language" -> "Language Challenge"
            "speech" -> "Speech Challenge"
            else -> "Motor Challenge"
        }
    }

    private fun normalizeLevel(level: String): String {
        return when (level.trim().lowercase()) {
            "beginner" -> "beginner"
            "intermediate" -> "intermediate"
            "advanced" -> "advanced"
            else -> "beginner"
        }
    }
}