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
import com.example.downsteps1.ui.MotorTestActivity
import com.example.downsteps1.R
import com.example.downsteps1.data.TestRepository
import com.example.downsteps1.data.local.LocalTestRepository
import com.example.downsteps1.model.TestQuestion
import com.google.android.material.button.MaterialButton

class LanguageTestActivity : BaseActivity() {

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

        // 🔥 حل اختفاء البنفسجي
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_language_test)

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
        questions.addAll(testRepository.getQuestionsByCategory("language"))
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
            Toast.makeText(
                this,
                getString(R.string.select_answer),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val selected = findViewById<RadioButton>(selectedId)
        totalScore += scoreFromAnswer(selected.text.toString())

        currentIndex++
        rgAnswers.clearCheck()

        if (currentIndex < questions.size) {
            showQuestion()
        } else {
            finishLanguageGoMotor()
        }
    }

    private fun showQuestion() {
        val currentQuestion = questions[currentIndex]

        tvQuestion.text = currentQuestion.questionText
        btnNext.text =
            if (currentIndex == questions.size - 1)
                getString(R.string.finish)
            else
                getString(R.string.next)

        val step = currentIndex + 1
        tvProgress.text = "$step/${questions.size}"
        progressQuestions.progress = step
    }

    private fun scoreFromAnswer(answer: String): Int {
        return when (answer) {
            getString(R.string.yes) -> 2
            getString(R.string.sometimes) -> 1
            getString(R.string.no) -> 0
            else -> 0
        }
    }

    private fun finishLanguageGoMotor() {
        val level = when {
            totalScore <= 3 -> "beginner"
            totalScore <= 7 -> "intermediate"
            else -> "advanced"
        }

        // --- حفظ النتيجة في Cloud Firestore ---
        val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()

            val languageResult = hashMapOf(
                "languageScore" to totalScore,
                "languageLevel" to level,
                "lastTestDate" to com.google.firebase.Timestamp.now()
            )

            db.collection("users").document(userId)
                .set(hashMapOf("languageAssessment" to languageResult), com.google.firebase.firestore.SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        getString(
                            R.string.language_assessment_saved,
                            translateLevel(level)
                        ),
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(this, MotorTestActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        this,
                        getString(
                            R.string.saved_locally_level,
                            translateLevel(level)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MotorTestActivity::class.java))
                    finish()
                }
        } else {
            startActivity(Intent(this, MotorTestActivity::class.java))
            finish()
        }
    }

    private fun translateLevel(level: String): String {
        return when (level.lowercase()) {
            "beginner" -> getString(R.string.beginner)
            "intermediate" -> getString(R.string.intermediate)
            "advanced" -> getString(R.string.advanced)
            else -> level
        }
    }
}