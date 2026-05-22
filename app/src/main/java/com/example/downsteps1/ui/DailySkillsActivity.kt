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
            openSkillVideos(
                getString(R.string.speech),
                TYPE_SPEECH
            )
        }

        itemWriting.setOnClickListener {
            openSkillVideos(
                getString(R.string.writing),
                TYPE_WRITING
            )
        }

        itemSelfCare.setOnClickListener {
            openSkillVideos(
                getString(R.string.self_care),
                TYPE_SELFCARE
            )
        }
        itemBehavior.setOnClickListener {
            openSkillVideos(
                getString(R.string.behavior),
                TYPE_BEHAVIOR
            )
        }
    }

    private fun openSkillVideos(skillTitle: String, skillType: String) {
        val intent = Intent(this, SkillVideosActivity::class.java)
        intent.putExtra("skill_title", skillTitle)
        intent.putExtra("skill_type", skillType)
        startActivity(intent)
    }

    private companion object {
        const val TYPE_SPEECH = "speech"
        const val TYPE_WRITING = "writing"
        const val TYPE_SELFCARE = "selfcare"
        const val TYPE_BEHAVIOR = "behavior"
    }
}