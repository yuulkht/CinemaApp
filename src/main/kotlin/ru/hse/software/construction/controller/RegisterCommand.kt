package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.auth.UserStorage
import ru.hse.software.construction.model.User
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.view.ConsoleOutputHandler

class RegisterCommand (
    private val userStorage: UserStorage,
    private val reader: ConsoleUserReader = ConsoleUserReader(),
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
) : Command {
    override fun process(programInfo: ProgramInfo) {
        execute(programInfo)
    }

    private fun execute(info: ProgramInfo) {
        if (info.authSession.isLoggedIn()) {
            outputHandler.displayMessage("Вы уже зарегистрированы")
        } else {
            outputHandler.displayMessage("Введите логин:")
            val login = reader.readAuthData()

            // Проверяем, что логин не занят
            if (userStorage.userExists(login)) {
                outputHandler.displayMessage("Пользователь с таким логином уже существует")
                return
            }

            outputHandler.displayMessage("Введите пароль:")
            val password = reader.readAuthData()

            userStorage.addUser(login, password)

            val user = User(login, password)
            info.authSession.login(user)

            outputHandler.displayMessage("Регистрация выполнена успешно.")
        }
    }
}