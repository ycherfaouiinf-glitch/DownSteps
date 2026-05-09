package com.example.downsteps1.model

data class ChallengeItem(
    val id: Int,
    val title: String,
    val description: String,
    val challengeType: String,
    val isLocked: Boolean
)