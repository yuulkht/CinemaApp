package ru.hse.software.construction.controller

import org.mindrot.jbcrypt.BCrypt
import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.auth.UserStorage
import ru.hse.software.construction.model.User

class LoginCommand(private val userStorage: UserStorage) : Command {
    override fun process(programInfo: ProgramInfo) {
        execute(programInfo)
    }

    private fun execute(info: ProgramInfo) {
        if (info.authSession.isLoggedIn()) {
            println("Вход в систему уже выполнен")
        } else {
            println("Введите логин:")
            val login = readln()
            println("Введите пароль:")
            val password = readln()

            if (userStorage.validatePassword(login, password)) {
                val user = User(login, password)
                info.authSession.login(user)
                println("Вход в систему выполнен успешно.")
            } else {
                println("Неверный логин или пароль.")
            }
        }
    }
}