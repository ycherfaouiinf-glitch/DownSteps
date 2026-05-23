package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.model.SkillVideo
import com.example.downsteps1.ui.adapter.SkillVideoAdapter
import com.google.firebase.firestore.FirebaseFirestore

class SkillVideosActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var txtSkillTitle: TextView
    private lateinit var txtSubTitle: TextView
    private lateinit var recyclerVideos: RecyclerView

    private lateinit var videoAdapter: SkillVideoAdapter
    private val videoList = mutableListOf<SkillVideo>()

    private val db = FirebaseFirestore.getInstance()

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
                videoResName = selectedVideo.videoResName
            )
        }

        recyclerVideos.layoutManager = LinearLayoutManager(this)
        recyclerVideos.adapter = videoAdapter
    }

    private fun setupHeader() {
        val skillTitle =
            intent.getStringExtra("skill_title")
                ?: getString(R.string.skill_videos)
        val skillType =
            intent.getStringExtra("skill_type")
                ?: TYPE_SPEECH

        txtSkillTitle.text = skillTitle
        txtSubTitle.text = getSubtitleBySkillType(skillType)
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadVideos() {
        val skillType =
            intent.getStringExtra("skill_type")
                ?: TYPE_SPEECH

        db.collection("skillVideos")
            .whereEqualTo("skillType", skillType)
            .get()
            .addOnSuccessListener { result ->

                videoList.clear()

                result.documents.forEachIndexed { index, doc ->

                    val video = SkillVideo(
                        id = index,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        skillType = doc.getString("skillType") ?: "",
                        videoResName = doc.getString("videoResName") ?: ""
                    )

                    videoList.add(video)
                }

                videoAdapter.notifyDataSetChanged()
            }
    }

    private fun getSubtitleBySkillType(skillType: String): String {
        return when (skillType) {
            TYPE_SPEECH -> getString(R.string.skill_speech_subtitle)
            TYPE_WRITING -> getString(R.string.skill_writing_subtitle)
            TYPE_SELFCARE -> getString(R.string.skill_selfcare_subtitle)
            TYPE_BEHAVIOR -> getString(R.string.skill_behavior_subtitle)
            else -> getString(R.string.skill_videos_subtitle)
        }
    }

    private companion object {
        const val TYPE_SPEECH = "speech"
        const val TYPE_WRITING = "writing"
        const val TYPE_SELFCARE = "selfcare"
        const val TYPE_BEHAVIOR = "behavior"
    }
}