package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo

class LogoutCommand : Command {
    override fun process(programInfo: ProgramInfo) {
        execute(programInfo)
    }

    private fun execute(info: ProgramInfo) {
        if (info.authSession.isLoggedIn()) {
            info.authSession.logout()
            println("Выход из системы выполнен успешно.")
        } else {
            println("Вы не в системе.")
        }
    }
}