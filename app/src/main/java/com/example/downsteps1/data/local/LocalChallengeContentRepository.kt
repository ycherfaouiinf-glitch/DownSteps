package com.example.downsteps1.data.local


import com.example.downsteps1.data.ChallengeContentRepository
import com.example.downsteps1.model.ChallengeContent

class LocalChallengeContentRepository : ChallengeContentRepository {

    private val challengeList = listOf(

        ChallengeContent(
            type = "motor",
            day = 1,
            level = "beginner",
            badgeType = "Motor Challenge",
            title = "Jumping on both feet",
            description = "Helps improve balance and coordination.",
            steps = listOf(
                "Stand in front of your child.",
                "Ask them to jump with both feet.",
                "Repeat 5 times."
            ),
            tip = "Keep it fun."
        ),

        ChallengeContent(
            type = "motor",
            day = 2,
            level = "beginner",
            badgeType = "Motor Challenge",
            title = "Walking on a line",
            description = "Improves balance and control.",
            steps = listOf(
                "Draw a line on the floor.",
                "Ask your child to walk on it.",
                "Repeat slowly."
            ),
            tip = "Hold their hand if needed."
        ),

        ChallengeContent(
            type = "language",
            day = 1,
            level = "beginner",
            badgeType = "Language Challenge",
            title = "Eye contact",
            description = "Encourages communication.",
            steps = listOf(
                "Hold a toy near your face.",
                "Call child's name.",
                "Wait for eye contact."
            ),
            tip = "Be patient."
        ),

        ChallengeContent(
            type = "language",
            day = 2,
            level = "beginner",
            badgeType = "Language Challenge",
            title = "Repeat words",
            description = "Improves pronunciation.",
            steps = listOf(
                "Say simple word.",
                "Child repeats.",
                "Repeat together."
            ),
            tip = "Speak slowly."
        ),

        ChallengeContent(
            type = "speech",
            day = 1,
            level = "beginner",
            badgeType = "Speech Challenge",
            title = "Lip movement",
            description = "Strengthens speech muscles.",
            steps = listOf(
                "Smile and relax.",
                "Repeat movement.",
                "Use mirror."
            ),
            tip = "Make it playful."
        ),

        ChallengeContent(
            type = "speech",
            day = 2,
            level = "beginner",
            badgeType = "Speech Challenge",
            title = "Blowing exercise",
            description = "Improves breath control.",
            steps = listOf(
                "Blow bubbles.",
                "Use paper or feather.",
                "Repeat."
            ),
            tip = "Short sessions."
        )
    )

    override fun getChallengeContent(challengeType: String, day: Int): ChallengeContent? {
        return challengeList.find {
            it.type == challengeType && it.day == day
        }
    }
}