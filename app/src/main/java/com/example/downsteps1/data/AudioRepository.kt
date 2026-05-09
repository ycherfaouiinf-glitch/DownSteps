package com.example.downsteps1.data

import com.example.downsteps1.model.AudioItem

interface AudioRepository {
    fun getAllAudio(): List<AudioItem>
    fun getFeaturedAudio(): AudioItem?
}