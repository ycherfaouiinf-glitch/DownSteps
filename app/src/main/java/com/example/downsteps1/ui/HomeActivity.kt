package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.data.CenterSeeder
import com.google.android.material.card.MaterialCardView

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homePage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        BottomNavHelper.setup(this, "home")

        val shouldShowSosActivation = getSharedPreferences("sos_prefs", MODE_PRIVATE)
            .getBoolean("show_sos_activation_after_tests", false)
        if (shouldShowSosActivation) {
            startActivity(Intent(this, SosWidgetActivationActivity::class.java))
            return
        }

        val cardFaq = findViewById<MaterialCardView>(R.id.cardFaq)
        val cardChallenges = findViewById<MaterialCardView>(R.id.cardChallenges)
        val cardSkills = findViewById<MaterialCardView>(R.id.cardSkills)
        val cardCenters = findViewById<MaterialCardView>(R.id.cardCenters)
        val cardGames = findViewById<MaterialCardView>(R.id.cardGames)
        val cardSuccessStories = findViewById<MaterialCardView>(R.id.cardSuccessStories)
        val cardAudioLibrary = findViewById<MaterialCardView>(R.id.cardAudioLibrary)

        cardFaq.setOnClickListener {
            startActivity(Intent(this, FaqActivity::class.java))
        }

        cardChallenges.setOnClickListener {
            startActivity(Intent(this, ChallengeTypeActivity::class.java))
        }

        cardSkills.setOnClickListener {
            startActivity(Intent(this, DailySkillsActivity::class.java))
        }

        cardCenters.setOnClickListener {
            startActivity(Intent(this, CentersAndAssociationsActivity::class.java))
        }

        cardGames.setOnClickListener {
            startActivity(Intent(this, GamesIntroActivity::class.java))
        }

        cardSuccessStories.setOnClickListener {
            startActivity(Intent(this, SuccessStoriesActivity::class.java))
        }

        cardAudioLibrary.setOnClickListener {
            startActivity(Intent(this, LibraryAudioActivity::class.java))
        }
    }
}