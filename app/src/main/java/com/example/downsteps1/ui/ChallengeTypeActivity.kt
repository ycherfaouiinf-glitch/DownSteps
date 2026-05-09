package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.google.android.material.card.MaterialCardView

class ChallengeTypeActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var cardMotor: MaterialCardView
    private lateinit var cardLanguage: MaterialCardView
    private lateinit var cardSpeech: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_type)

        BottomNavHelper.setup(this, "home")
        initViews()
        setupClicks()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        cardMotor = findViewById(R.id.cardMotor)
        cardLanguage = findViewById(R.id.cardLanguage)
        cardSpeech = findViewById(R.id.cardSpeech)
    }

    private fun setupClicks() {
        btnBack.setOnClickListener {
            finish()
        }

        cardMotor.setOnClickListener {
            openChallengeTimeline("motor")
        }

        cardLanguage.setOnClickListener {
            openChallengeTimeline("language")
        }

        cardSpeech.setOnClickListener {
            openChallengeTimeline("speech")
        }
    }

    private fun openChallengeTimeline(type: String) {
        val intent = Intent(this, ChallengesTimelineActivity::class.java)
        intent.putExtra("challenge_type", type)
        startActivity(intent)
    }
}