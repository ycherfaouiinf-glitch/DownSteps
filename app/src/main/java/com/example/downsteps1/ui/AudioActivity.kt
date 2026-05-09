package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper

class AudioActivity : BaseActivity() {

    private lateinit var backContainer: LinearLayout
    private lateinit var btnPrevious: ImageView
    private lateinit var btnPlay: ImageView
    private lateinit var btnNext: ImageView

    private lateinit var tvAudioTitle: TextView
    private lateinit var tvAudioDuration: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var audioProgress: ProgressBar

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    private val handler = Handler(Looper.getMainLooper())

    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    val currentPosition = player.currentPosition
                    val duration = player.duration

                    if (duration > 0) {
                        val progress = (currentPosition * 100) / duration
                        audioProgress.progress = progress
                        tvCurrentTime.text = formatTime(currentPosition)
                        tvTotalTime.text = formatTime(duration)
                    }

                    handler.postDelayed(this, 500)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_audio)

        WindowCompat.getInsetsController(window, window.decorView)
            ?.isAppearanceLightStatusBars = true

        BottomNavHelper.setup(this, "home")

        initializeViews()
        loadAudioData()
        setupClickListeners()
    }

    private fun initializeViews() {
        backContainer = findViewById(R.id.backContainer)

        btnPrevious = findViewById(R.id.btnPrevious)
        btnPlay = findViewById(R.id.btnPlay)
        btnNext = findViewById(R.id.btnNext)

        tvAudioTitle = findViewById(R.id.tvAudioTitle)
        tvAudioDuration = findViewById(R.id.tvAudioDuration)
        tvCurrentTime = findViewById(R.id.tvCurrentTime)
        tvTotalTime = findViewById(R.id.tvTotalTime)
        audioProgress = findViewById(R.id.audioProgress)
    }

    private fun loadAudioData() {
        val audioTitle = intent.getStringExtra(EXTRA_AUDIO_TITLE) ?: "Story Title"
        val audioDuration = intent.getStringExtra(EXTRA_AUDIO_DURATION) ?: "03:25"

        tvAudioTitle.text = audioTitle
        tvAudioDuration.text = audioDuration
        tvCurrentTime.text = "0:00"
        tvTotalTime.text = audioDuration

        audioProgress.max = 100
        audioProgress.progress = 0

        prepareDemoAudio()
    }

    private fun prepareDemoAudio() {
        // لاحقًا عندما تضيفين ملف صوتي داخل res/raw:
        // mediaPlayer = MediaPlayer.create(this, R.raw.little_lion_story)

        mediaPlayer?.setOnCompletionListener {
            isPlaying = false
            btnPlay.setImageResource(android.R.drawable.ic_media_play)
            audioProgress.progress = 100
            handler.removeCallbacks(updateProgressRunnable)
        }
    }

    private fun setupClickListeners() {
        backContainer.setOnClickListener {
            finish()
        }

        btnPlay.setOnClickListener {
            togglePlayState()
        }

        btnPrevious.setOnClickListener {
            // لاحقًا: تشغيل الصوت السابق
        }

        btnNext.setOnClickListener {
            // لاحقًا: تشغيل الصوت التالي
        }
    }

    private fun togglePlayState() {
        val player = mediaPlayer ?: return

        if (isPlaying) {
            player.pause()
            isPlaying = false
            btnPlay.setImageResource(android.R.drawable.ic_media_play)
            handler.removeCallbacks(updateProgressRunnable)
        } else {
            player.start()
            isPlaying = true
            btnPlay.setImageResource(android.R.drawable.ic_media_pause)
            handler.post(updateProgressRunnable)
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateProgressRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    companion object {
        const val EXTRA_AUDIO_ID = "extra_audio_id"
        const val EXTRA_AUDIO_TITLE = "extra_audio_title"
        const val EXTRA_AUDIO_DURATION = "extra_audio_duration"
        const val EXTRA_AUDIO_URL = "extra_audio_url"
        const val EXTRA_AUDIO_IMAGE = "extra_audio_image"
    }
}