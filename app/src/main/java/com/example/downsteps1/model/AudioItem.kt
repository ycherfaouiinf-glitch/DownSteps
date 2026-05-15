package com.example.downsteps1.model

data class AudioItem(
    val id: Int = 0,
    val title: String = "",
    val duration: String = "",
    val description: String = "",
    val audioName: String = "",
    val imageName: String = "",
    val featured: Boolean = false,
    val audioResId: Int = 0,
    val imageResId: Int = 0
)