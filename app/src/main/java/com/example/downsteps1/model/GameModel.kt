package com.example.downsteps1.model

data class GameModel(
    val name: String,
    val description: String,
    val suggestedBenefit: String,
    val level: String,
    val imageResId: Int,
    val backgroundColor: String,
    val buttonColor: String,
    val levelBgColor: String,
    val levelTextColor: String,
    val isPopular: Boolean = false,
    var isFavorite: Boolean = false
)