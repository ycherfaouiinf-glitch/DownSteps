package com.example.downsteps1.data.local

import com.example.downsteps1.data.AuthRepository

class LocalAuthRepository : AuthRepository {

    override fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            onResult(true, null)
        } else {
            onResult(false, "Please enter email and password")
        }
    }

    override fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            onResult(true, null)
        } else {
            onResult(false, "Fill all fields")
        }
    }

    override fun sendResetCode(
        email: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (email.isNotEmpty()) {
            onResult(true, null)
        } else {
            onResult(false, "Enter your email")
        }
    }

    override fun loginWithGoogle(
        token: String,
        onResult: (Boolean, Boolean) -> Unit
    ) {
        if (token.isNotEmpty()) {
            onResult(true, false)
        } else {
            onResult(false, false)
        }
    }
}