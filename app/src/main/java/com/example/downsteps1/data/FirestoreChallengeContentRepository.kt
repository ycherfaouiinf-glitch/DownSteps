package com.example.downsteps1.data

import com.example.downsteps1.model.ChallengeContent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreChallengeContentRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getChallengeFromFirebase(
        type: String,
        day: Int,
        level: String
    ): ChallengeContent? {
        return try {
            val snapshot = db.collection("challenges")
                .whereEqualTo("type", type)
                .whereEqualTo("day", day)
                .whereEqualTo("level", level)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(ChallengeContent::class.java)

        } catch (e: Exception) {
            null
        }
    }
}