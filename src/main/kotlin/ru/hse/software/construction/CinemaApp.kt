package ru.hse.software.construction

import ru.hse.software.construction.controller.*
import ru.hse.software.construction.view.ConsoleOutputHandler

class Program {
    companion object {

        private val info: ProgramInfo = ProgramInfo()
        private val isFilledInfo = info.isFilledInfo()

        private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
        private var exit: Boolean = false

        private var commands: MutableMap<String, Command> = mutableMapOf()

        init {
            commands["register"] = RegisterCommand(info.userStorage)
            commands["login"] = LoginCommand(info.userStorage)
            commands["logout"] = LogoutCommand()
            commands["1"] = Command1()
            commands["2"] = Command2()
            commands["3"] = Command3()
            commands["4"] = Command4()
            commands["5"] = Command5()
            commands["6"] = Command6()
        }

        fun processCommand(command: String) {
            if (command == "q") {
                exit = true
                return
            }

            if (!commands.containsKey(command)) {
                outputHandler.displayMessage("Неопознанная команда")
                return
            }

            if (info.authSession.isLoggedIn() && (commands[command] is LoginCommand || commands[command] is RegisterCommand)) {
                outputHandler.displayMessage("Вход уже был выполнен")
                return
            }

            if (info.authSession.isLoggedIn() || commands[command] is LoginCommand || commands[command] is RegisterCommand) {
                commands[command]!!.process(info)
            } else {
                outputHandler.displayMessage("Требуется вход в систему или регистрация")
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            if (!isFilledInfo) {
                outputHandler.displayError("проблема с доступом к базе данных кинотеатра")
                exit = true
            }
            if (!exit) {
                outputHandler.displayMessage("Добро пожаловать в приложение Кинотеатр!")
            }
            while (!exit) {
                if (!info.authSession.isLoggedIn()) {
                    outputHandler.displayAuthCommands()
                } else {
                    outputHandler.displayCommands()
                }

                outputHandler.displayMessage("Введите команду (или 'q' для выхода): ")

                val command = readln()
                processCommand(command)
            }
        }
    }
}