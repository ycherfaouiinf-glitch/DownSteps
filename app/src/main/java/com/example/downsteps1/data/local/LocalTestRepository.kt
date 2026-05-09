package com.example.downsteps1.data.local

import com.example.downsteps1.data.TestRepository
import com.example.downsteps1.model.TestQuestion

class LocalTestRepository : TestRepository {

    override fun getQuestionsByCategory(category: String): List<TestQuestion> {
        return when (category) {
            "language" -> listOf(
                TestQuestion(1, "Can your child say simple words like mama or baba?", "language"),
                TestQuestion(
                    2,
                    "Does your child understand simple commands like come or sit?",
                    "language"
                ),
                TestQuestion(
                    3,
                    "Can your child name familiar objects like water or ball?",
                    "language"
                ),
                TestQuestion(
                    4,
                    "Can your child form a short sentence of two words like I want water?",
                    "language"
                ),
                TestQuestion(5, "Can your child repeat words after you when asked?", "language")
            )

            "motor" -> listOf(
                TestQuestion(1, "Can your child sit without support?", "motor"),
                TestQuestion(2, "Can your child stand alone for a few seconds?", "motor"),
                TestQuestion(3, "Can your child walk without help?", "motor"),
                TestQuestion(4, "Can your child run for a short distance?", "motor"),
                TestQuestion(5, "Can your child climb stairs with or without help?", "motor"),
                TestQuestion(6, "Can your child go down the stairs?", "motor"),
                TestQuestion(7, "Can your child jump in place?", "motor"),
                TestQuestion(8, "Can your child kick a small ball?", "motor"),
                TestQuestion(9, "Can your child pick up small objects using fingers?", "motor"),
                TestQuestion(10, "Can your child hold a spoon or pencil properly?", "motor")
            )

            "speech" -> listOf(
                TestQuestion(
                    1,
                    "Does your child pronounce clear sounds like ba / ma / da?",
                    "speech"
                ),
                TestQuestion(2, "Can your child say two-syllable sounds like ma-ma?", "speech"),
                TestQuestion(
                    3,
                    "Does your child say words that are understandable to others most of the time?",
                    "speech"
                ),
                TestQuestion(
                    4,
                    "Does your child have difficulty pronouncing some letters like S / R / SH?",
                    "speech"
                ),
                TestQuestion(
                    5,
                    "Can your child repeat a word after hearing it from you?",
                    "speech"
                ),
                TestQuestion(
                    6,
                    "Can your child form words with 2 to 3 syllables correctly?",
                    "speech"
                ),
                TestQuestion(
                    7,
                    "Does your child speak in short sentences without dropping most sounds?",
                    "speech"
                ),
                TestQuestion(
                    8,
                    "Does pronunciation improve with repetition and practice?",
                    "speech"
                )
            )

            else -> emptyList()
        }
    }
}