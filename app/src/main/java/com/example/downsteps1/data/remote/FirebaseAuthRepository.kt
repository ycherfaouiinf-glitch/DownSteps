package com.example.downsteps1.data.remote

import com.example.downsteps1.data.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseAuthRepository : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message ?: "Login failed")
                }
            }
    }


    override fun loginWithGoogle(token: String, onResult: (Boolean, Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {

                    db.collection("users").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                // مستخدم قديم
                                onResult(true, false)
                            } else {
                                // مستخدم جديد
                                onResult(true, true)
                            }
                        }
                        .addOnFailureListener { onResult(false, false) }
                }
            } else {
                onResult(false, false)
            }
        }
    }

    override fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener onResult(false, "User ID error")

                    val userData = mapOf(
                        "name" to name,
                        "email" to email,
                        "isProfileComplete" to false
                    )


                    db.collection("users").document(userId).set(userData)
                        .addOnCompleteListener {
                            // The account was created successfully. Do not block the onboarding
                            // flow if Firestore is slow/offline or its rules reject this optional save.
                            onResult(true, null)
                        }
                } else {
                    onResult(false, task.exception?.message ?: "Signup failed")
                }
            }
    }

    override fun sendResetCode(
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message ?: "Reset password failed")
                }
            }
    }
}