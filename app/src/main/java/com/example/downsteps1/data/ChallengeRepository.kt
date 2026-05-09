package com.example.downsteps1.data

import com.example.downsteps1.model.ChallengeItem

interface ChallengeRepository {
    fun getChallengesByType(type: String): List<ChallengeItem>
}