package com.example.downsteps1.model

data class ChallengeContent(
    var id: String = "",
    val type: String = "",
    val level: String = "",
    val day: Int = 0,
    val badgeType: String = "",
    val title: String = "",
    val description: String = "",
    val steps: List<String> = emptyList()
)