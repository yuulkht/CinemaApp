package ru.hse.software.construction.auth

import org.mindrot.jbcrypt.BCrypt
import ru.hse.software.construction.model.User
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import ru.hse.software.construction.repository.FileUserStorageRepository

@JsonSerialize
class UserStorage (
    private val users: MutableList<User> = mutableListOf()
) {
    // for serializer
    fun getUsers() : MutableList<User> {
        return users
    }

    fun addUser(login: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        users.add(User(login, hashedPassword))
        FileUserStorageRepository().saveUserStorage(this)
    }

    private fun getUserPassword(login: String): String? {
        val user = users.find { it.getLogin() == login }
        return user?.getHashedPassword()
    }

    fun userExists(login: String): Boolean {
        return users.any { it.getLogin() == login }
    }

    fun validatePassword(login: String, password: String): Boolean {
        val hashedPassword = getUserPassword(login)
        return hashedPassword != null && BCrypt.checkpw(password, hashedPassword)
    }
}