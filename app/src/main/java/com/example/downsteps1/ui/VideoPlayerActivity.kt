package com.example.downsteps1.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.downsteps1.R
import com.example.downsteps1.common.ui.BaseActivity

class VideoPlayerActivity : BaseActivity() {

    private lateinit var playerView: PlayerView
    private lateinit var btnBackVideo: ImageView
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        setContentView(R.layout.activity_video_player)

        hideSystemUI()

        playerView = findViewById(R.id.playerView)
        btnBackVideo = findViewById(R.id.btnBackVideo)

        btnBackVideo.setOnClickListener {
            finish()
        }

        val videoResName = intent.getStringExtra(EXTRA_VIDEO_RES_NAME) ?: ""
        val resId = resources.getIdentifier(videoResName, "raw", packageName)

        if (resId != 0) {
            playVideo(resId)
        }
    }

    private fun playVideo(resId: Int) {
        player = ExoPlayer.Builder(this).build()

        val videoUri = Uri.parse("android.resource://$packageName/$resId")
        val mediaItem = MediaItem.fromUri(videoUri)

        playerView.player = player
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.play()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }

    companion object {
        private const val EXTRA_VIDEO_RES_NAME = "video_res_name"

        fun start(context: Context, title: String, videoResName: String) {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra(EXTRA_VIDEO_RES_NAME, videoResName)
            context.startActivity(intent)
        }
    }
}