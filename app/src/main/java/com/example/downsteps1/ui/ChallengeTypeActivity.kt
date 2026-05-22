package com.example.downsteps1.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.google.android.material.card.MaterialCardView

class ChallengeTypeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_type)

        BottomNavHelper.setup(this, "home")

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val cardMotor = findViewById<MaterialCardView>(R.id.cardMotor)
        val cardLanguage = findViewById<MaterialCardView>(R.id.cardLanguage)
        val cardSpeech = findViewById<MaterialCardView>(R.id.cardSpeech)

        btnBack.setOnClickListener { finish() }

        cardMotor.setOnClickListener {
            openTimeline(TYPE_MOTOR)
        }

        cardLanguage.setOnClickListener {
            openTimeline(TYPE_LANGUAGE)
        }

        cardSpeech.setOnClickListener {
            openTimeline(TYPE_SPEECH)
        }
    }

    private fun openTimeline(type: String) {
        val intent = Intent(this, ChallengesTimelineActivity::class.java)
        intent.putExtra("challenge_type", type)
        startActivity(intent)
    }

    private companion object {
        const val TYPE_MOTOR = "motor"
        const val TYPE_LANGUAGE = "language"
        const val TYPE_SPEECH = "speech"
    }
}