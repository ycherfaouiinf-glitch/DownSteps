package com.example.downsteps1.ui

import android.content.Intent
import com.example.downsteps1.common.ui.BaseActivity
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.model.AudioItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.find
class AudioActivity : BaseActivity() {

    private lateinit var backContainer: LinearLayout
    private lateinit var btnPrevious: ImageView
    private lateinit var btnPlay: ImageView
    private lateinit var btnNext: ImageView

    private lateinit var tvAudioTitle: TextView
    private lateinit var tvAudioDuration: TextView
    private lateinit var tvCurrentTime: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var audioProgress: SeekBar

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var imgMainCover: ImageView
    private var audioId: Int = 0
    private var audioName: String = ""
    private var imageName: String = ""

    private val audioPrefs by lazy {
        getSharedPreferences("audio_prefs", MODE_PRIVATE)
    }

    private var lastPreviousClickTime = 0L

    private val db = FirebaseFirestore.getInstance()
    private var audioList: List<AudioItem> = emptyList()

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
        loadAudios()
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
        imgMainCover = findViewById(R.id.imgMainCover)
    }

    private fun loadAudioData() {
        val audioTitle = intent.getStringExtra(EXTRA_AUDIO_TITLE) ?: "Story Title"
        val audioDuration = intent.getStringExtra(EXTRA_AUDIO_DURATION) ?: "03:25"

        audioName = intent.getStringExtra("audio_name") ?: "rabbit_story"
        imageName = intent.getStringExtra("image_name") ?: "rabbit_story_cover"


        tvAudioTitle.text = audioTitle
        tvAudioDuration.text = audioDuration
        tvCurrentTime.text = "0:00"
        tvTotalTime.text = audioDuration

        audioProgress.max = 100
        audioProgress.progress = 0

        imgMainCover.setImageResource(getAudioImage(imageName))

        prepareAudio()

        val savedAudioName = audioPrefs.getString("last_audio_name", "")
        val savedPosition = audioPrefs.getInt("last_audio_position", 0)

        if (savedAudioName == audioName && savedPosition > 0) {
            mediaPlayer?.seekTo(savedPosition)
            tvCurrentTime.text = formatTime(savedPosition)

            mediaPlayer?.let { player ->
                if (player.duration > 0) {
                    audioProgress.progress = (savedPosition * 100) / player.duration
                }
            }
        }

        audioId = intent.getIntExtra(EXTRA_AUDIO_ID, 1)
    }


    private fun prepareAudio() {
        val audioRes = getAudioResource(audioName)

        mediaPlayer = MediaPlayer.create(this, audioRes)

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

            val currentTime = System.currentTimeMillis()

            if (currentTime - lastPreviousClickTime < 600) {
                playPreviousStory()
            } else {
                mediaPlayer?.seekTo(0)
                audioProgress.progress = 0
                tvCurrentTime.text = "0:00"
            }

            lastPreviousClickTime = currentTime
        }

        btnNext.setOnClickListener {
            playNextStory()
        }

        audioProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.let { player ->
                        val newPosition = (player.duration * progress) / 100
                        player.seekTo(newPosition)
                        tvCurrentTime.text = formatTime(newPosition)
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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
        saveAudioProgress()
        handler.removeCallbacks(updateProgressRunnable)
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
    override fun onPause() {
        super.onPause()
        saveAudioProgress()
    }

    companion object {
        const val EXTRA_AUDIO_ID = "extra_audio_id"
        const val EXTRA_AUDIO_TITLE = "extra_audio_title"
        const val EXTRA_AUDIO_DURATION = "extra_audio_duration"
        const val EXTRA_AUDIO_URL = "extra_audio_url"
        const val EXTRA_AUDIO_IMAGE = "extra_audio_image"
    }

    private fun getAudioResource(audioName: String): Int {
        val resourceId = resources.getIdentifier(
            audioName,
            "raw",
            packageName
        )

        return if (resourceId != 0) {
            resourceId
        } else {
            R.raw.rabbit_story
        }
    }

    private fun getAudioImage(imageName: String): Int {

        val resourceId = resources.getIdentifier(
            imageName,
            "drawable",
            packageName
        )

        return if (resourceId != 0) {
            resourceId
        } else {
            android.R.drawable.ic_media_play
        }
    }

    private fun saveAudioProgress() {
        val player = mediaPlayer ?: return

        audioPrefs.edit()
            .putInt("last_audio_id", audioId)
            .putString("last_audio_title", tvAudioTitle.text.toString())
            .putString("last_audio_duration", tvAudioDuration.text.toString())
            .putString("last_audio_name", audioName)
            .putString("last_audio_image", imageName)
            .putInt("last_audio_position", player.currentPosition)
            .putInt("last_audio_duration_ms", player.duration)
            .apply()
    }


    private fun playNextStory() {
        db.collection("audios")
            .get()
            .addOnSuccessListener { snapshot ->

                val stories = snapshot.documents.mapNotNull { doc ->
                    AudioItem(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        duration = doc.getString("duration") ?: "",
                        description = doc.getString("description") ?: "",
                        audioName = doc.getString("audioName") ?: "",
                        imageName = doc.getString("imageName") ?: ""
                    )
                }.sortedBy { it.id }

                val currentIndex = stories.indexOfFirst { it.id == audioId }

                if (currentIndex != -1 && currentIndex < stories.size - 1) {
                    openStory(stories[currentIndex + 1])
                }
            }
    }

    private fun openStory(story: AudioItem) {
        saveAudioProgress()

        val intent = Intent(this, AudioActivity::class.java).apply {
            putExtra(EXTRA_AUDIO_ID, story.id)
            putExtra(EXTRA_AUDIO_TITLE, story.title)
            putExtra(EXTRA_AUDIO_DURATION, story.duration)
            putExtra("audio_name", story.audioName)
            putExtra("image_name", story.imageName)
        }

        startActivity(intent)
        finish()
    }

    private fun playPreviousStory() {
        db.collection("audios")
            .get()
            .addOnSuccessListener { snapshot ->

                val stories = snapshot.documents.mapNotNull { doc ->
                    AudioItem(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        duration = doc.getString("duration") ?: "",
                        description = doc.getString("description") ?: "",
                        audioName = doc.getString("audioName") ?: "",
                        imageName = doc.getString("imageName") ?: ""
                    )
                }.sortedBy { it.id }

                val currentIndex = stories.indexOfFirst { it.id == audioId }

                if (currentIndex > 0) {
                    openStory(stories[currentIndex - 1])
                }
            }
    }

    private fun loadAudios() {

        db.collection("audios")
            .orderBy("id")
            .get()
            .addOnSuccessListener { snapshot ->

                audioList = snapshot.documents.mapNotNull { doc ->

                    AudioItem(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        duration = doc.getString("duration") ?: "",
                        description = doc.getString("description") ?: "",
                        audioName = doc.getString("audioName") ?: "",
                        imageName = doc.getString("imageName") ?: ""
                    )
                }
            }
    }
}