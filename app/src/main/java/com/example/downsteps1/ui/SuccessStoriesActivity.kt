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
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class SuccessStoriesActivity : BaseActivity() {

    private lateinit var recyclerStories: RecyclerView
    private val db = FirebaseFirestore.getInstance()

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

        recyclerStories.layoutManager = LinearLayoutManager(this)
        loadStoriesFromFirebase()
    }

    private fun loadStoriesFromFirebase() {
        db.collection("successStories")
            .get()
            .addOnSuccessListener { snapshot ->

                val stories = snapshot.documents.mapNotNull { doc ->
                    val imageName = doc.getString("imageName") ?: "succesahmed"

                    SuccessStory(
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        category = doc.getString("category") ?: "",
                        year = doc.getString("year") ?: "",
                        imageName = imageName,
                        imageRes = getStoryImage(imageName)
                    )
                }

                recyclerStories.adapter = SuccessStoryAdapter(ArrayList(stories))
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading stories", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getStoryImage(imageName: String): Int {

        if (imageName.isBlank()) {
            return 0
        }

        return resources.getIdentifier(
            imageName,
            "drawable",
            packageName
        )
    }
}
