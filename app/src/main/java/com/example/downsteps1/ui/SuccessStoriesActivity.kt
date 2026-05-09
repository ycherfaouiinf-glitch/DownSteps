package com.example.downsteps1.ui

import com.example.downsteps1.common.ui.BaseActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.downsteps1.R
import com.example.downsteps1.common.navigation.BottomNavHelper
import com.example.downsteps1.data.remote.SuccessStory
import com.example.downsteps1.data.remote.SuccessStoryAdapter

class SuccessStoriesActivity : BaseActivity() {

    private lateinit var recyclerStories: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_success_stories)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.successStoriesPage)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        BottomNavHelper.setup(this, "home")

        recyclerStories = findViewById(R.id.recyclerStories)

        val stories = arrayListOf(
            SuccessStory(
                "Ahmed, from student to inspiration",
                "Ahmed loves learning and community participation. With determination and encouragement, he continues building his future with confidence.",
                "Education",
                "2023",
                R.drawable.succesahmed
            ),
            SuccessStory(
                "Sara, a champion at heart",
                "Sara discovered her love for swimming early. Through practice and support, she became a source of pride and motivation for others.",
                "Sports",
                "2022",
                R.drawable.successara
            ),
            SuccessStory(
                "Lila, an artist of dreams",
                "Lila expresses herself through colors and creativity. Her artwork reflects joy, emotion, and beautiful imagination.",
                "Arts",
                "2021",
                R.drawable.successara
            ),
            SuccessStory(
                "Khaled, working with passion",
                "Khaled enjoys helping others and taking responsibility. His journey shows that independence grows step by step.",
                "Work",
                "2023",
                R.drawable.successara
            )
        )

        recyclerStories.layoutManager = LinearLayoutManager(this)
        recyclerStories.adapter = SuccessStoryAdapter(stories)
    }
}