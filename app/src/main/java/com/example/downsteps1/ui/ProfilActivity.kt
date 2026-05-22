package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilActivity : BaseActivity() {

    // تعريف العناصر البرمجية
    private lateinit var tvChildName: TextView
    private lateinit var tvChildNameTop: TextView
    private lateinit var tvChildGender: TextView
    private lateinit var tvChildBirthDate: TextView
    private lateinit var tvMotorLevel: TextView
    private lateinit var tvLanguageLevel: TextView
    private lateinit var tvSpeechLevel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profil)

        BottomNavHelper.setup(this, "profile")

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profil)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeViews()
        fetchChildDataFromFirestore()

        findViewById<Button>(R.id.btnEditChild).setOnClickListener {
            startActivity(Intent(this, EditChildActivity::class.java))
        }

        findViewById<Button>(R.id.btnReEvaluate).setOnClickListener {
            // العودة لأول مرحلة تقييم (اللغة)
            startActivity(Intent(this, LanguageTestActivity::class.java))
        }
    }

    private fun initializeViews() {
        tvChildName = findViewById(R.id.tvChildName)
        tvChildNameTop = findViewById(R.id.tvChildNameTop)
        tvChildGender = findViewById(R.id.tvChildGender)
        tvChildBirthDate = findViewById(R.id.tvChildBirthDate)
        tvMotorLevel = findViewById(R.id.tvMotorLevel)
        tvLanguageLevel = findViewById(R.id.tvLanguageLevel)
        tvSpeechLevel = findViewById(R.id.tvSpeechLevel)
    }

    private fun fetchChildDataFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()

            // استخدام addSnapshotListener بدلاً من get لجعل التحديث لحظياً
            db.collection("users").document(userId)
                .addSnapshotListener { document, error ->
                    if (error != null) {
                        Toast.makeText(
                            this,
                            getString(R.string.error_message, error.message ?: ""),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addSnapshotListener
                    }

                    if (document != null && document.exists()) {
                        val name = document.getString("childName") ?: getString(R.string.unknown)
                        val genderRaw = document.getString("gender") ?: ""
                        val birth = document.getString("birthDate") ?: getString(R.string.not_set)

                        val motorRaw = document.getString("motorAssessment.motorLevel") ?: ""
                        val languageRaw = document.getString("languageAssessment.languageLevel") ?: ""
                        val speechRaw = document.getString("speechAssessment.level") ?: ""

                        val gender = translateGender(genderRaw)
                        val motor = translateLevel(motorRaw)
                        val language = translateLevel(languageRaw)
                        val speech = translateLevel(speechRaw)

                        // تحديث العناصر في الواجهة
                        tvChildName.text = name
                        tvChildNameTop.text = name
                        tvChildGender.text = gender
                        tvChildBirthDate.text = birth

                        tvMotorLevel.text = getString(R.string.motor_level_format, motor)
                        tvLanguageLevel.text = getString(R.string.language_level_format, language)
                        tvSpeechLevel.text = getString(R.string.speech_level_format, speech)
                    }
                }
        }
    }

    private fun translateGender(gender: String): String {
        return when (gender.uppercase()) {
            "MALE" -> getString(R.string.male)
            "FEMALE" -> getString(R.string.female)
            else -> getString(R.string.not_set)
        }
    }

    private fun translateLevel(level: String): String {
        return when (level.lowercase()) {
            "beginner" -> getString(R.string.beginner)
            "intermediate" -> getString(R.string.intermediate)
            "advanced" -> getString(R.string.advanced)
            else -> getString(R.string.not_tested)
        }
    }
}