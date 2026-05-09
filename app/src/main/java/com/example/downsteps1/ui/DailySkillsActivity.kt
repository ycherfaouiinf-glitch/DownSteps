package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.ui.SkillVideosActivity

class DailySkillsActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var itemSpeech: LinearLayout
    private lateinit var itemWriting: LinearLayout
    private lateinit var itemSelfCare: LinearLayout
    private lateinit var itemBehavior: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_skills)

        BottomNavHelper.setup(this, "home")


        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        itemSpeech = findViewById(R.id.itemSpeech)
        itemWriting = findViewById(R.id.itemWriting)
        itemSelfCare = findViewById(R.id.itemSelfCare)
        itemBehavior = findViewById(R.id.itemBehavior)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        itemSpeech.setOnClickListener {
            openSkillVideos("Speech", "speech")
        }

        itemWriting.setOnClickListener {
            openSkillVideos("Writing", "writing")
        }

        itemSelfCare.setOnClickListener {
            openSkillVideos("Self Care", "selfcare")
        }

        itemBehavior.setOnClickListener {
            openSkillVideos("Behavior", "behavior")
        }
    }

    private fun openSkillVideos(skillTitle: String, skillType: String) {
        val intent = Intent(this, SkillVideosActivity::class.java)
        intent.putExtra("skill_title", skillTitle)
        intent.putExtra("skill_type", skillType)
        startActivity(intent)
    }
}