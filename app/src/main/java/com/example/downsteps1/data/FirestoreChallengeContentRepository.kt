package com.example.downsteps1.data

import com.example.downsteps1.model.ChallengeContent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreChallengeContentRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getChallengesByTypeAndLevel(
        type: String,
        level: String
    ): List<ChallengeContent> {
        val snapshot = db.collection("challenges")
            .whereEqualTo("type", type)
            .whereEqualTo("level", level.lowercase())
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(ChallengeContent::class.java)?.apply {
                id = doc.id
            }
        }.sortedBy { it.day }
    }

    suspend fun getChallengeDetails(
        type: String,
        level: String,
        day: Int
    ): ChallengeContent? {
        val snapshot = db.collection("challenges")
            .whereEqualTo("type", type)
            .whereEqualTo("level", level.lowercase())
            .whereEqualTo("day", day)
            .get()
            .await()

        return snapshot.documents.firstOrNull()
            ?.toObject(ChallengeContent::class.java)
    }
}