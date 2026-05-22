package com.example.downsteps1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.data.FirestoreChallengeContentRepository
import com.example.downsteps1.model.ChallengeContent
import com.example.downsteps1.model.ChallengeItem
import com.example.downsteps1.ui.adapter.ChallengeAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChallengesTimelineActivity : BaseActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val repository = FirestoreChallengeContentRepository()

    private var challengeType = CHALLENGE_MOTOR
    private var level = LEVEL_BEGINNER
    private var currentDay = 1
    private var lastDate = ""

    private lateinit var recyclerChallenges: RecyclerView
    private lateinit var txtType: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_timeline)

        BottomNavHelper.setup(this, "home")

        challengeType =
            intent.getStringExtra("challenge_type")
                ?: CHALLENGE_MOTOR

        recyclerChallenges = findViewById(R.id.recyclerChallenges)
        txtType = findViewById(R.id.txtType)
        btnBack = findViewById(R.id.btnBack)

        txtType.text = when (challengeType) {
            "motor" -> getString(R.string.motor_challenge)
            "language" -> getString(R.string.language_challenge)
            "speech" -> getString(R.string.speech_challenge)
            else -> getString(R.string.challenges)
        }

        btnBack.setOnClickListener { finish() }

        recyclerChallenges.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(
                this,
                getString(R.string.user_not_logged_in),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { doc ->

                level = when (challengeType) {
                    "motor" -> {
                        doc.getString("motorLevel")
                            ?: doc.getString("motorAssessment.motorLevel")
                            ?: LEVEL_BEGINNER
                    }

                    "language" -> {
                        doc.getString("languageLevel")
                            ?: doc.getString("languageAssessment.languageLevel")
                            ?: LEVEL_BEGINNER
                    }

                    "speech" -> {
                        doc.getString("speechLevel")
                            ?: doc.getString("speechAssessment.level")
                            ?: LEVEL_BEGINNER
                    }

                    else -> "beginner"
                }.lowercase()

                currentDay = when (challengeType) {
                    "motor" -> doc.getLong("currentDayMotor")?.toInt() ?: 1
                    "language" -> doc.getLong("currentDayLanguage")?.toInt() ?: 1
                    "speech" -> doc.getLong("currentDaySpeech")?.toInt() ?: 1
                    else -> 1
                }

                lastDate = when (challengeType) {
                    "motor" -> doc.getString("lastDateMotor") ?: ""
                    "language" -> doc.getString("lastDateLanguage") ?: ""
                    "speech" -> doc.getString("lastDateSpeech") ?: ""
                    else -> ""
                }

                loadChallenges()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.error_loading_user_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun loadChallenges() {
        lifecycleScope.launch {
            try {
                val challenges = repository.getChallengesByTypeAndLevel(
                    type = challengeType,
                    level = level
                )

                showChallenges(challenges)

            } catch (e: Exception) {
                Toast.makeText(
                    this@ChallengesTimelineActivity,
                    getString(
                        R.string.error_message,
                        e.message ?: ""
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showChallenges(challenges: List<ChallengeContent>) {
        val doneToday = lastDate == getTodayDate()

        val challengeItems = challenges.map { challenge ->
            val locked = challenge.day > currentDay ||
                    (doneToday && challenge.day == currentDay)

            ChallengeItem(
                id = challenge.day,
                title =
                    if (locked)
                        getString(
                            R.string.day_format,
                            challenge.day
                        )
                    else
                        challenge.title,
                description = if (locked) {
                    getString(R.string.locked)
                } else if (challenge.day < currentDay) {
                    getString(R.string.completed)
                } else {
                    getString(R.string.today)
                },
                challengeType = challenge.type,
                isLocked = locked
            )
        }

        val adapter = ChallengeAdapter(
            challenges = challengeItems,
            challengeType = challengeType,
            onChallengeClick = { challenge ->
                val intent = Intent(this, Challange1Activity::class.java)
                intent.putExtra("challenge_type", challengeType)
                intent.putExtra("challenge_level", level)
                intent.putExtra("challenge_day", challenge.id)
                intent.putExtra("is_completed", challenge.id < currentDay)
                startActivity(intent)
            }
        )

        recyclerChallenges.adapter = adapter
    }

    private fun getTodayDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private companion object {
        const val CHALLENGE_MOTOR = "motor"
        const val LEVEL_BEGINNER = "beginner"
    }
}