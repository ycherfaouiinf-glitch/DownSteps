package com.example.downsteps1.data.local

import com.example.downsteps1.data.AudioRepository
import com.example.downsteps1.model.AudioItem

class LocalAudioRepository : AudioRepository {

    private val audioList = listOf(

        AudioItem(
            id = 1,
            title = "Little Lion Story",
            duration = "03:25",
            description = "Calm bedtime story",
            audioName = "rabbit_story",
            imageName = "rabbit_story_cover",
            featured = true
        )
    )

    override fun getAllAudio(): List<AudioItem> {
        return audioList
    }

    override fun getFeaturedAudio(): AudioItem? {
        return audioList.firstOrNull()
    }
}