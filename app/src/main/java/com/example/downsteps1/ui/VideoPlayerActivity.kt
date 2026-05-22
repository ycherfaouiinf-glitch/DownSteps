package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.downsteps1.R

class VideoPlayerActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvVideoTitle: TextView
    private lateinit var tvPlaceholder: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        initializeViews()
        setupData()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        tvVideoTitle = findViewById(R.id.tvVideoTitle)
        tvPlaceholder = findViewById(R.id.tvPlaceholder)
    }

    private fun setupData() {
        val videoTitle =
            intent.getStringExtra(EXTRA_VIDEO_TITLE)
                ?: getString(R.string.video)
        val videoUrl = intent.getStringExtra(EXTRA_VIDEO_URL) ?: ""

        tvVideoTitle.text = videoTitle

        tvPlaceholder.text =
            if (videoUrl.isNotEmpty()) {
                getString(
                    R.string.video_url_placeholder,
                    videoUrl
                )
            } else {
                getString(R.string.video_player_placeholder)
            }
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val EXTRA_VIDEO_TITLE = "video_title"
        private const val EXTRA_VIDEO_URL = "video_url"

        fun start(context: Context, title: String, videoUrl: String) {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_TITLE, title)
            intent.putExtra(EXTRA_VIDEO_URL, videoUrl)
            context.startActivity(intent)
        }
    }
}