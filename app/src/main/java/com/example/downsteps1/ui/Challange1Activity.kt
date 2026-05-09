package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.downsteps1.R
import com.example.downsteps1.data.FirestoreChallengeContentRepository // تأكدي من إنشاء هذا الملف
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.SystemBarHelper
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class Challange1Activity : BaseActivity() {


    private val firestoreRepository = FirestoreChallengeContentRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarHelper.makeTransparent(this)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContentView(R.layout.activity_challange1)
        BottomNavHelper.setup(this, "home")
        val backContainer = findViewById<LinearLayout>(R.id.backContainer)
        val txtBadgeType = findViewById<TextView>(R.id.txtBadgeType)
        val txtChallengeTitle = findViewById<TextView>(R.id.txtChallengeTitle)
        val txtChallengeDesc = findViewById<TextView>(R.id.txtChallengeDesc)
        val imgChallenge = findViewById<ImageView>(R.id.imgChallenge)
        val stepsContainer = findViewById<LinearLayout>(R.id.stepsContainer)
        val btnDone = findViewById<MaterialButton>(R.id.btnDone)
        val level = intent.getStringExtra("challenge_level") ?: "beginner"

        val challengeType = intent.getStringExtra("challenge_type") ?: "motor"
        val day = intent.getIntExtra("challenge_day", 1)


        backContainer.setOnClickListener { finish() }


        lifecycleScope.launch {
            try {

                val content = firestoreRepository.getChallengeFromFirebase(challengeType, day, level)

                if (content != null) {

                    txtBadgeType.text = content.badgeType
                    txtChallengeTitle.text = content.title
                    txtChallengeDesc.text = content.description


                    when (challengeType) {
                        "motor" -> imgChallenge.setImageResource(R.drawable.kid_motor)
                        "language" -> imgChallenge.setImageResource(R.drawable.languagetype)
                        "speech" -> imgChallenge.setImageResource(R.drawable.speechtype)
                    }

                    stepsContainer.removeAllViews()
                    content.steps.forEachIndexed { index, stepText ->
                        val stepView = LayoutInflater.from(this@Challange1Activity)
                            .inflate(R.layout.item_challenge_step, stepsContainer, false)

                        val txtStepNum = stepView.findViewById<TextView>(R.id.txtStepNum)
                        val txtStepText = stepView.findViewById<TextView>(R.id.txtStepText)

                        txtStepNum.text = (index + 1).toString()
                        txtStepText.text = stepText

                        stepsContainer.addView(stepView)
                    }
                } else {
                    Toast.makeText(this@Challange1Activity, "Challenge details not found in Firebase", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@Challange1Activity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


        val isCompleted = intent.getBooleanExtra("is_completed", false)

        if (isCompleted) {
            btnDone.text = "Back to Timeline"
            btnDone.setOnClickListener { finish() }
        } else {
            btnDone.text = "Done"
            btnDone.setOnClickListener {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val db = FirebaseFirestore.getInstance()

                    val dayField = when (challengeType) {
                        "motor" -> "currentDayMotor"
                        "language" -> "currentDayLanguage"
                        "speech" -> "currentDaySpeech"
                        else -> "currentDayMotor"
                    }
                    val dateField = when (challengeType) {
                        "motor" -> "lastDateMotor"
                        "language" -> "lastDateLanguage"
                        "speech" -> "lastDateSpeech"
                        else -> "lastDateMotor"
                    }


                    val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())


                    val updates = mapOf(
                        dayField to (day + 1),
                        dateField to todayDate
                    )

                    db.collection("users").document(userId)
                        .update(updates)
                        .addOnSuccessListener {
                            Toast.makeText(this@Challange1Activity, "Great job! The next challenge opens tomorrow.", Toast.LENGTH_LONG).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@Challange1Activity, "Error saving progress.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}