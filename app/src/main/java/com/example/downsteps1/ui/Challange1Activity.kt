package com.example.downsteps1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.common.ui.SystemBarHelper
import com.example.downsteps1.data.FirestoreChallengeContentRepository
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Challange1Activity : BaseActivity() {

    private val repository = FirestoreChallengeContentRepository()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var challengeType = CHALLENGE_MOTOR
    private var level = LEVEL_BEGINNER
    private var day = 1
    private var isCompleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarHelper.makeTransparent(this)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_challange1)

        BottomNavHelper.setup(this, "home")

        challengeType = intent.getStringExtra("challenge_type") ?: "motor"
        level = intent.getStringExtra("challenge_level") ?: "beginner"
        day = intent.getIntExtra("challenge_day", 1)
        isCompleted = intent.getBooleanExtra("is_completed", false)

        val backContainer = findViewById<LinearLayout>(R.id.backContainer)
        val txtBadgeType = findViewById<TextView>(R.id.txtBadgeType)
        val txtTitle = findViewById<TextView>(R.id.txtChallengeTitle)
        val txtDescription = findViewById<TextView>(R.id.txtChallengeDesc)
        val imgChallenge = findViewById<ImageView>(R.id.imgChallenge)
        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)
        val btnDone = findViewById<MaterialButton>(R.id.btnDone)

        backContainer.setOnClickListener { finish() }

        loadDetails(
            txtBadgeType,
            txtTitle,
            txtDescription,
            imgChallenge,
            stepsContainer
        )

        if (isCompleted) {
            btnDone.text = getString(R.string.back)
            btnDone.setOnClickListener { finish() }
        } else {
            btnDone.text = getString(R.string.done)
            btnDone.setOnClickListener {
                saveDone()
            }
        }
    }

    private fun loadDetails(
        txtBadgeType: TextView,
        txtTitle: TextView,
        txtDescription: TextView,
        imgChallenge: ImageView,
        stepsContainer: LinearLayout
    ) {
        lifecycleScope.launch {
            try {
                val challenge = repository.getChallengeDetails(
                    type = challengeType,
                    level = level,
                    day = day
                )

                if (challenge == null) {
                    Toast.makeText(
                        this@Challange1Activity,
                        getString(R.string.challenge_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                txtBadgeType.text = challenge.badgeType
                txtTitle.text = challenge.title
                txtDescription.text = challenge.description

                when (challengeType) {
                    "motor" -> imgChallenge.setImageResource(R.drawable.kid_motor)
                    "language" -> imgChallenge.setImageResource(R.drawable.languagetype)
                    "speech" -> imgChallenge.setImageResource(R.drawable.speechtype)
                }

                stepsContainer.removeAllViews()

                challenge.steps.forEachIndexed { index, step ->
                    val stepView = LayoutInflater.from(this@Challange1Activity)
                        .inflate(R.layout.item_challenge_step, stepsContainer, false)

                    val txtStepNum = stepView.findViewById<TextView>(R.id.txtStepNum)
                    val txtStepText = stepView.findViewById<TextView>(R.id.txtStepText)

                    txtStepNum.text = "${index + 1}"
                    txtStepText.text = step

                    stepsContainer.addView(stepView)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@Challange1Activity,
                    getString(
                        R.string.error_message,
                        e.message ?: ""
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveDone() {
        val userId = auth.currentUser?.uid

        if (userId == null) {
            Toast.makeText(
                this,
                getString(R.string.user_not_logged_in),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val currentDayField: String
        val lastDateField: String

        when (challengeType) {
            "motor" -> {
                currentDayField = "currentDayMotor"
                lastDateField = "lastDateMotor"
            }

            "language" -> {
                currentDayField = "currentDayLanguage"
                lastDateField = "lastDateLanguage"
            }

            "speech" -> {
                currentDayField = "currentDaySpeech"
                lastDateField = "lastDateSpeech"
            }

            else -> {
                currentDayField = "currentDayMotor"
                lastDateField = "lastDateMotor"
            }
        }

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val updates = mapOf(
            currentDayField to day + 1,
            lastDateField to today
        )

        db.collection("users").document(userId)
            .update(updates)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    getString(R.string.challenge_completed),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    getString(R.string.error_saving_progress),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private companion object {
        const val CHALLENGE_MOTOR = "motor"
        const val LEVEL_BEGINNER = "beginner"
    }
}