package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.ui.VideoPlayerActivity
import com.example.downsteps1.data.SkillsRepository
import com.example.downsteps1.data.local.LocalSkillsRepository
import com.example.downsteps1.model.SkillVideo
import com.example.downsteps1.ui.adapter.SkillVideoAdapter

class SkillVideosActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var txtSkillTitle: TextView
    private lateinit var txtSubTitle: TextView
    private lateinit var recyclerVideos: RecyclerView

    private lateinit var videoAdapter: SkillVideoAdapter
    private val videoList = mutableListOf<SkillVideo>()

    private val skillsRepository: SkillsRepository = LocalSkillsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skill_videos)


        initializeViews()
        setupRecyclerView()
        setupHeader()
        setupClickListeners()
        loadVideos()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        txtSkillTitle = findViewById(R.id.txtSkillTitle)
        txtSubTitle = findViewById(R.id.txtSubTitle)
        recyclerVideos = findViewById(R.id.recyclerVideos)
    }

    private fun setupRecyclerView() {
        videoAdapter = SkillVideoAdapter(videoList) { selectedVideo ->
            VideoPlayerActivity.Companion.start(
                context = this,
                title = selectedVideo.title,
                videoUrl = selectedVideo.videoUrl
            )
        }

        recyclerVideos.layoutManager = LinearLayoutManager(this)
        recyclerVideos.adapter = videoAdapter
    }

    private fun setupHeader() {
        val skillTitle = intent.getStringExtra("skill_title") ?: "Skill Videos"
        val skillType = intent.getStringExtra("skill_type") ?: "speech"

        txtSkillTitle.text = skillTitle
        txtSubTitle.text = getSubtitleBySkillType(skillType)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadVideos() {
        val skillType = intent.getStringExtra("skill_type") ?: "speech"
        val videos = skillsRepository.getVideosBySkillType(skillType)

        videoList.clear()
        videoList.addAll(videos)
        videoAdapter.notifyDataSetChanged()
    }

    private fun getSubtitleBySkillType(skillType: String): String {
        return when (skillType) {
            "speech" -> "Activities to improve speaking and pronunciation skills."
            "writing" -> "Activities to improve early writing skills."
            "selfcare" -> "Activities to support daily self-care routines."
            "behavior" -> "Activities to support positive behavior."
            else -> "Activities to support your child’s development."
        }
    }
}