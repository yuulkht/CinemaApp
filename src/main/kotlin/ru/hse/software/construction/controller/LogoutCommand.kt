package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.view.ConsoleOutputHandler

class LogoutCommand (
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
): Command {
    override fun process(programInfo: ProgramInfo) {
        execute(programInfo)
    }

    private fun execute(info: ProgramInfo) {
        if (info.authSession.isLoggedIn()) {
            info.authSession.logout()
            outputHandler.displayMessage("Выход из системы выполнен успешно.")
        } else {
            outputHandler.displayMessage("Вы не в системе.")
        }
    }
}