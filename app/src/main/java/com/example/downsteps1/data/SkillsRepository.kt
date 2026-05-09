package com.example.downsteps1.data

import com.example.downsteps1.model.SkillCategory
import com.example.downsteps1.model.SkillVideo

interface SkillsRepository {
    fun getSkillCategories(): List<SkillCategory>
    fun getVideosBySkillType(skillType: String): List<SkillVideo>
}