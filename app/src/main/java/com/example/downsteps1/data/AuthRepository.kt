package com.example.downsteps1.data

interface AuthRepository {

    fun login(
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    )

    fun signup(
        name: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    )

    fun sendResetCode(
        email: String,
        onResult: (Boolean, String?) -> Unit
    )


    fun loginWithGoogle(
        token: String,
        onResult: (isSuccess: Boolean, isNewUser: Boolean) -> Unit
    )

}