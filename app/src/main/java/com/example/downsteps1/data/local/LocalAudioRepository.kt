package com.example.downsteps1.data.local

import com.example.downsteps1.data.AudioRepository
import com.example.downsteps1.model.AudioItem

class LocalAudioRepository : AudioRepository {

    private val audioList = listOf(
        AudioItem(
            id = 1,
            title = "Little Lion Story",
            duration = "03:25",
            audioResId = null,
            imageResId = null,
            audioUrl = "little_lion_story_url"
        ),
        AudioItem(
            id = 2,
            title = "Sleepy Moon Story",
            duration = "02:40",
            audioResId = null,
            imageResId = null,
            audioUrl = "sleepy_moon_story_url"
        ),
        AudioItem(
            id = 3,
            title = "The Quiet Forest",
            duration = "04:10",
            audioResId = null,
            imageResId = null,
            audioUrl = "the_quiet_forest_url"
        )
    )

    override fun getAllAudio(): List<AudioItem> {
        return audioList
    }

    override fun getFeaturedAudio(): AudioItem? {
        return audioList.firstOrNull()
    }
}