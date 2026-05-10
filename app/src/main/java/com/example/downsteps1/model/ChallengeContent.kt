package com.example.downsteps1.model

data class ChallengeContent(
    val type: String = "",
    val day: Int = 0,
    val level: String = "",
    val badgeType: String = "",
    val title: String = "",
    val description: String = "",
    val imageRes: Int = 0,
    val steps: List<String> = emptyList(),
    val tip: String = ""
)//