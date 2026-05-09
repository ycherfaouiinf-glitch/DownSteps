package com.example.downsteps1.data.local

import com.example.downsteps1.data.ChallengeRepository
import com.example.downsteps1.model.ChallengeItem

class LocalChallengeRepository : ChallengeRepository {

    override fun getChallengesByType(type: String): List<ChallengeItem> {
        val list = mutableListOf<ChallengeItem>()

        for (i in 1..30) {
            list.add(
                ChallengeItem(
                    id = i,
                    title = "Challenge $i",
                    description = if (i == 1) "Start here" else "Locked",
                    challengeType = type,
                    isLocked = i != 1
                )
            )
        }

        return list
    }
}