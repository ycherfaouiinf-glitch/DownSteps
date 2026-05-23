package com.example.downsteps1.model

data class SkillVideo(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val skillType: String = "",
    val videoUrl: String = "",
    val videoResName: String = "",
    val thumbnailResId: Int? = null
)