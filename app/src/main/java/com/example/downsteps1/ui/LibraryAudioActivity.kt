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

class LibraryAudioActivity : BaseActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var btnPlayFeatured: ImageView
    private lateinit var recyclerAudioList: RecyclerView
    private lateinit var etSearchAudio: TextInputEditText

    private lateinit var tvCurrentAudioTitle: TextView
    private lateinit var tvCurrentDuration: TextView

    private lateinit var audioAdapter: AudioAdapter
    private var featuredAudio: AudioItem? = null

    private val audioRepository = RepositoryProvider.provideAudioRepository()
    private var audioList: List<AudioItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemBarHelper.makeTransparent(this)
        setContentView(R.layout.activity_library_audio)

        BottomNavHelper.setup(this, "home")

        initializeViews()
        loadAudios()
        loadFeaturedAudio()
        setupRecyclerView()
        setupClickListeners()
    }

    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        btnPlayFeatured = findViewById(R.id.btnPlayFeatured)
        recyclerAudioList = findViewById(R.id.recyclerAudioList)
        etSearchAudio = findViewById(R.id.etSearchAudio)

        tvCurrentAudioTitle = findViewById(R.id.tvCurrentAudioTitle)
        tvCurrentDuration = findViewById(R.id.tvCurrentDuration)
    }

    private fun loadAudios() {
        audioList = audioRepository.getAllAudio()
    }

    private fun loadFeaturedAudio() {
        featuredAudio = audioRepository.getFeaturedAudio()

        tvCurrentAudioTitle.text = featuredAudio?.title ?: "Story Title"
        tvCurrentDuration.text = featuredAudio?.duration ?: "0:00"
    }

    private fun setupRecyclerView() {
        audioAdapter = AudioAdapter(audioList) { selectedAudio ->
            featuredAudio = selectedAudio
            tvCurrentAudioTitle.text = selectedAudio.title
            tvCurrentDuration.text = selectedAudio.duration
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

        btnPlayFeatured.setOnClickListener {
            featuredAudio?.let { audioItem ->
                openAudioPlayer(audioItem)
            }
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
    }

    private fun openAudioPlayer(audioItem: AudioItem) {
        val intent = Intent(this, AudioActivity::class.java).apply {
            putExtra(AudioActivity.EXTRA_AUDIO_ID, audioItem.id)
            putExtra(AudioActivity.EXTRA_AUDIO_TITLE, audioItem.title)
            putExtra(AudioActivity.EXTRA_AUDIO_DURATION, audioItem.duration)
            putExtra(AudioActivity.EXTRA_AUDIO_URL, audioItem.audioUrl)
        }
        startActivity(intent)
    }
}