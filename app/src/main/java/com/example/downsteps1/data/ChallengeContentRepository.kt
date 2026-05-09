package com.example.downsteps1.data

import com.example.downsteps1.model.ChallengeContent

interface ChallengeContentRepository {
    fun getChallengeContent(challengeType: String, day: Int): ChallengeContent?
}