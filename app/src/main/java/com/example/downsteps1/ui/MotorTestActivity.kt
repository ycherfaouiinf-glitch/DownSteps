package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.downsteps1.R
import com.example.downsteps1.ui.SpeechTestActivity
import com.example.downsteps1.data.TestRepository
import com.example.downsteps1.data.local.LocalTestRepository
import com.example.downsteps1.model.TestQuestion
import com.google.android.material.button.MaterialButton

class MotorTestActivity : BaseActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var rgAnswers: RadioGroup
    private lateinit var btnNext: MaterialButton
    private lateinit var tvProgress: TextView
    private lateinit var progressQuestions: ProgressBar

    private val testRepository: TestRepository = LocalTestRepository()
    private val questions = mutableListOf<TestQuestion>()

    private var currentIndex = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_motor_test)

        WindowCompat.getInsetsController(window, window.decorView)
            ?.isAppearanceLightStatusBars = true

        initializeViews()
        loadQuestions()
        setupScreen()
        setupClickListeners()
    }

    private fun initializeViews() {
        tvQuestion = findViewById(R.id.tvQuestion)
        rgAnswers = findViewById(R.id.rgAnswers)
        btnNext = findViewById(R.id.btnNext)
        tvProgress = findViewById(R.id.tvProgress)
        progressQuestions = findViewById(R.id.progressQuestions)
    }

    private fun loadQuestions() {
        questions.clear()
        questions.addAll(testRepository.getQuestionsByCategory("motor"))
    }

    private fun setupScreen() {
        progressQuestions.max = questions.size
        showQuestion()
    }

    private fun setupClickListeners() {
        btnNext.setOnClickListener {
            handleNextQuestion()
        }
    }

    private fun handleNextQuestion() {
        val selectedId = rgAnswers.checkedRadioButtonId

        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selected = findViewById<RadioButton>(selectedId)
        totalScore += scoreFromAnswer(selected.text.toString())

        currentIndex++
        rgAnswers.clearCheck()

        if (currentIndex < questions.size) {
            showQuestion()
        } else {
            finishMotorGoSpeech()
        }
    }

    private fun showQuestion() {
        val currentQuestion = questions[currentIndex]

        tvQuestion.text = currentQuestion.questionText
        btnNext.text = if (currentIndex == questions.size - 1) "Finish" else "Next"

        val step = currentIndex + 1
        tvProgress.text = "$step/${questions.size}"
        progressQuestions.progress = step
    }

    private fun scoreFromAnswer(answer: String): Int {
        return when (answer) {
            "Yes" -> 2
            "Sometimes" -> 1
            "No" -> 0
            else -> 0
        }
    }

    private fun finishMotorGoSpeech() {
        val level = when {
            totalScore <= 6 -> "Beginner"
            totalScore <= 14 -> "Intermediate"
            else -> "Advanced"
        }

        val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()


            val motorResult = hashMapOf(
                "motorScore" to totalScore,
                "motorLevel" to level,
                "lastTestDate" to com.google.firebase.Timestamp.now()
            )

            db.collection("users").document(userId)
                .set(hashMapOf("motorAssessment" to motorResult), com.google.firebase.firestore.SetOptions.merge()) // دمج لضمان عدم حذف بيانات اللغة [cite: 180]
                .addOnSuccessListener {
                    Toast.makeText(this, "Motor assessment saved!\nLevel: $level", Toast.LENGTH_LONG).show()
                    // الانتقال للاختبار التالي (Speech)
                    startActivity(Intent(this, SpeechTestActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(this, "Proceeding to next step...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SpeechTestActivity::class.java))
                    finish()
                }
        } else {
            startActivity(Intent(this, SpeechTestActivity::class.java))
            finish()
        }
    }
}