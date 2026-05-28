package com.example.downsteps1.model

data class FaqItem(
    val question: String,
    val answer: String,
    val keywords: String,
    var isExpanded: Boolean = false
)