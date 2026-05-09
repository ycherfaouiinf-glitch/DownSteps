package com.example.downsteps1.data

import com.example.downsteps1.model.TestQuestion

interface TestRepository {
    fun getQuestionsByCategory(category: String): List<TestQuestion>
}