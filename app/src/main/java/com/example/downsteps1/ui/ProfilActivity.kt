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
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    if (document != null && document.exists()) {
                        // جلب البيانات وتحديث الواجهة فوراً
                        val name = document.getString("childName") ?: "Unknown"
                        val gender = document.getString("gender") ?: "Not set"
                        val birth = document.getString("birthDate") ?: "Not set"

                        val motor = document.getString("motorAssessment.motorLevel") ?: "Not tested"
                        val language = document.getString("languageAssessment.languageLevel") ?: "Not tested"
                        val speech = document.getString("speechAssessment.level") ?: "Not tested"

                        // تحديث العناصر في الواجهة
                        tvChildName.text = name
                        tvChildNameTop.text = name
                        tvChildGender.text = gender
                        tvChildBirthDate.text = birth

                        tvMotorLevel.text = "Motor: $motor"
                        tvLanguageLevel.text = "Language: $language"
                        tvSpeechLevel.text = "Speech: $speech"
                    }
                }
        }
    }
}