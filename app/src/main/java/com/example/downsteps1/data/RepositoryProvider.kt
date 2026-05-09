package com.example.downsteps1.data

import com.example.downsteps1.data.local.LocalAudioRepository
import com.example.downsteps1.data.local.LocalChallengeContentRepository
import com.example.downsteps1.data.local.LocalChallengeRepository
import com.example.downsteps1.data.local.LocalSkillsRepository
import com.example.downsteps1.data.local.LocalTestRepository

object RepositoryProvider {

    fun provideAudioRepository(): AudioRepository {
        return LocalAudioRepository()
    }

    fun provideChallengeRepository(): ChallengeRepository {
        return LocalChallengeRepository()
    }

    fun provideChallengeContentRepository(): ChallengeContentRepository {
        return LocalChallengeContentRepository()
    }

    fun provideSkillsRepository(): SkillsRepository {
        return LocalSkillsRepository()
    }

    fun provideTestRepository(): TestRepository {
        return LocalTestRepository()
    }
}