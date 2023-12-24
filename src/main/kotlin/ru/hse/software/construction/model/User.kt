package ru.hse.software.construction.model

import org.mindrot.jbcrypt.BCrypt
import sun.security.util.Password
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
class User(
    private val login: String = "",
    private val hashedPassword: String = ""
) {

    fun getLogin() : String {
        return login
    }

    fun getHashedPassword() : String {
        return hashedPassword
    }
}