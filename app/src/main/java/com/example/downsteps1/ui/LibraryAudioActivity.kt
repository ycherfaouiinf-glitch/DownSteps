package com.example.downsteps1.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.common.ui.BaseActivity
import com.example.downsteps1.common.ui.SystemBarHelper
import com.example.downsteps1.data.RepositoryProvider
import com.example.downsteps1.model.AudioItem
import com.example.downsteps1.ui.adapter.AudioAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.ProgressBar
import android.view.View

class LibraryAudioActivity : BaseActivity() {

    private lateinit var btnBack: ImageView

    private lateinit var imgFeaturedCover: ImageView
    private lateinit var recyclerAudioList: RecyclerView
    private lateinit var etSearchAudio: TextInputEditText

    private lateinit var tvCurrentAudioTitle: TextView
    private lateinit var tvCurrentDuration: TextView

    private lateinit var audioAdapter: AudioAdapter
    private var featuredAudio: AudioItem? = null

    private val audioRepository = RepositoryProvider.provideAudioRepository()
    private var audioList: List<AudioItem> = emptyList()

    private val db = FirebaseFirestore.getInstance()

    private val audioPrefs by lazy {
        getSharedPreferences("audio_prefs", MODE_PRIVATE)
    }
    private lateinit var featuredProgress: ProgressBar

    private lateinit var currentAudioCard: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarHelper.makeTransparent(this)
        setContentView(R.layout.activity_library_audio)

        BottomNavHelper.setup(this, "home")

        initializeViews()

        setupRecyclerView()

        loadAudios()

        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadAudios()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        imgFeaturedCover = findViewById(R.id.imgFeaturedCover)
        recyclerAudioList = findViewById(R.id.recyclerAudioList)
        etSearchAudio = findViewById(R.id.etSearchAudio)

        tvCurrentAudioTitle = findViewById(R.id.tvCurrentAudioTitle)
        tvCurrentDuration = findViewById(R.id.tvCurrentDuration)
        featuredProgress = findViewById(R.id.featuredProgress)
        currentAudioCard = findViewById(R.id.currentAudioCard)
    }

    private fun loadAudios() {

        db.collection("audios")
            .get()
            .addOnSuccessListener { snapshot ->

                audioList = snapshot.documents.mapNotNull { doc ->

                    AudioItem(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        title = doc.getString("title") ?: "",
                        duration =
                            doc.getString("duration")
                                ?: getString(R.string.audio_default_duration),
                        description = doc.getString("description") ?: "",
                        audioName = doc.getString("audioName") ?: "",
                        imageName = doc.getString("imageName") ?: "",
                        featured = doc.getBoolean("featured") ?: false,
                        imageResId = getAudioImage(doc.getString("imageName") ?: "")
                    )
                }.sortedBy { it.id }

                etSearchAudio.setText("")
                audioAdapter.updateList(audioList)
                loadFeaturedAudio()
            }
    }

    private fun loadFeaturedAudio() {
        val lastTitle = audioPrefs.getString("last_audio_title", null)
        val lastDuration = audioPrefs.getString("last_audio_duration", null)
        val lastAudioName = audioPrefs.getString("last_audio_name", null)
        val lastImageName = audioPrefs.getString("last_audio_image", null)

        val lastAudioId = audioPrefs.getInt("last_audio_id", 0)

        featuredAudio = if (lastTitle != null && lastAudioName != null && lastAudioId != 0) {


            AudioItem(
                id = lastAudioId,
                title = lastTitle,
                duration =
                    lastDuration
                        ?: getString(R.string.audio_default_duration),
                audioName = lastAudioName,
                imageName = lastImageName ?: "",
                imageResId = getAudioImage(lastImageName ?: "")
            )

        } else {
            audioList.find { it.featured } ?: audioList.firstOrNull()
        }

        tvCurrentAudioTitle.text =
            featuredAudio?.title
                ?: getString(R.string.story_title)
        tvCurrentDuration.text =
            featuredAudio?.duration
                ?: getString(R.string.audio_default_duration)

        imgFeaturedCover.setImageResource(
            featuredAudio?.imageResId ?: android.R.drawable.ic_media_play
        )

        val position = audioPrefs.getInt("last_audio_position", 0)
        val durationMs = audioPrefs.getInt("last_audio_duration_ms", 0)

        tvCurrentDuration.text =
            if (durationMs > 0) {
                "${formatTime(position)} / ${formatTime(durationMs)}"
            } else {
                featuredAudio?.duration
                    ?: getString(R.string.audio_default_duration)
            }

        featuredProgress.progress =
            if (durationMs > 0) (position * 100) / durationMs else 0


    }

    private fun setupRecyclerView() {
        audioAdapter = AudioAdapter(audioList) { selectedAudio ->
            featuredAudio = selectedAudio
            openAudioPlayer(selectedAudio)
        }

        recyclerAudioList.layoutManager = LinearLayoutManager(this)
        recyclerAudioList.adapter = audioAdapter
    }

    private fun filterAudio() {
        val query = etSearchAudio.text?.toString()?.trim()?.lowercase().orEmpty()

        val filteredList = if (query.isEmpty()) {
            audioList
        } else {
            audioList.filter { audio ->
                matchesFromStart(audio.title, query) ||
                        matchesFromStart(audio.duration, query)
            }
        }

        audioAdapter.updateList(filteredList)
    }

    private fun matchesFromStart(text: String, query: String): Boolean {
        return text
            .lowercase()
            .split(" ", "-", "_", ":", ".")
            .any { word ->
                word.startsWith(query)
            }
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        etSearchAudio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                filterAudio()
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })

        currentAudioCard.setOnClickListener {
            featuredAudio?.let {
                openAudioPlayer(it)
            }
        }

        imgFeaturedCover.setOnClickListener {
            featuredAudio?.let { audioItem ->
                openAudioPlayer(audioItem)
            }
        }
    }

    private fun openAudioPlayer(audioItem: AudioItem) {
        val intent = Intent(this, AudioActivity::class.java).apply {
            putExtra(AudioActivity.EXTRA_AUDIO_ID, audioItem.id)
            putExtra(AudioActivity.EXTRA_AUDIO_TITLE, audioItem.title)
            putExtra(AudioActivity.EXTRA_AUDIO_DURATION, audioItem.duration)
            putExtra("audio_name", audioItem.audioName)
            putExtra("image_name", audioItem.imageName)
        }
        startActivity(intent)
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
    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }



}