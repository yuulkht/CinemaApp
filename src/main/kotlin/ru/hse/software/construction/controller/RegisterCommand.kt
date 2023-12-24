package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.auth.UserStorage
import ru.hse.software.construction.model.User

class RegisterCommand(private val userStorage: UserStorage) : Command {
    override fun process(programInfo: ProgramInfo) {
        execute(programInfo)
    }

    private fun execute(info: ProgramInfo) {
        if (info.authSession.isLoggedIn()) {
            println("Вы уже зарегистрированы")
        } else {
            println("Введите логин:")
            val login = readln()

            // Проверяем, что логин не занят
            if (userStorage.userExists(login)) {
                println("Пользователь с таким логином уже существует")
                return
            }

            println("Введите пароль:")
            val password = readln()

            userStorage.addUser(login, password)

            val user = User(login, password)
            info.authSession.login(user)

            println("Регистрация выполнена успешно.")
        }
    }
}