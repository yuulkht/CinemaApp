package ru.hse.software.construction.auth

import ru.hse.software.construction.model.User

class AuthSession (
    private var currentUser: User? = null
) {

    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}