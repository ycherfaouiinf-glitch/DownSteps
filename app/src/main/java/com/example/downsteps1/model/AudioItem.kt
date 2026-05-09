package com.example.downsteps1.model

data class AudioItem(
    val id: Int,
    val title: String,
    val duration: String,
    val audioResId: Int? = null,
    val imageResId: Int? = null,
    val audioUrl: String? = null,
    val imageUrl: String? = null
)