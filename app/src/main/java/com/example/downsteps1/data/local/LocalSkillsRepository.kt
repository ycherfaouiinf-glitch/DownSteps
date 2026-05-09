package com.example.downsteps1.data.local

import com.example.downsteps1.data.SkillsRepository
import com.example.downsteps1.model.SkillCategory
import com.example.downsteps1.model.SkillVideo

class LocalSkillsRepository : SkillsRepository {

    override fun getSkillCategories(): List<SkillCategory> {
        return listOf(
            SkillCategory(1, "Speech", "speech"),
            SkillCategory(2, "Writing", "writing"),
            SkillCategory(3, "Self Care", "selfcare"),
            SkillCategory(4, "Behavior", "behavior")
        )
    }

    override fun getVideosBySkillType(skillType: String): List<SkillVideo> {
        return when (skillType) {
            "speech" -> listOf(
                SkillVideo(
                    id = 1,
                    title = "Letter Pronunciation Practice",
                    description = "A simple activity to help children pronounce letters clearly.",
                    skillType = "speech",
                    videoUrl = "speech_video_1"
                ),
                SkillVideo(
                    id = 2,
                    title = "Easy Word Repetition",
                    description = "Practice repeating short and familiar words.",
                    skillType = "speech",
                    videoUrl = "speech_video_2"
                )
            )

            "writing" -> listOf(
                SkillVideo(
                    id = 3,
                    title = "Learn to Write Letters",
                    description = "A guided activity for tracing and writing letters.",
                    skillType = "writing",
                    videoUrl = "writing_video_1"
                ),
                SkillVideo(
                    id = 4,
                    title = "Handwriting Practice",
                    description = "A simple exercise to improve pencil control and writing.",
                    skillType = "writing",
                    videoUrl = "writing_video_2"
                )
            )

            "selfcare" -> listOf(
                SkillVideo(
                    id = 5,
                    title = "Washing Hands",
                    description = "Teach the child the correct handwashing steps.",
                    skillType = "selfcare",
                    videoUrl = "selfcare_video_1"
                ),
                SkillVideo(
                    id = 6,
                    title = "Brushing Teeth",
                    description = "A basic routine to build healthy hygiene habits.",
                    skillType = "selfcare",
                    videoUrl = "selfcare_video_2"
                )
            )

            "behavior" -> listOf(
                SkillVideo(
                    id = 7,
                    title = "Respecting Others",
                    description = "A simple lesson on polite and respectful behavior.",
                    skillType = "behavior",
                    videoUrl = "behavior_video_1"
                ),
                SkillVideo(
                    id = 8,
                    title = "Managing Anger",
                    description = "Practical steps to help children calm down and react better.",
                    skillType = "behavior",
                    videoUrl = "behavior_video_2"
                )
            )

            else -> emptyList()
        }
    }
}