package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.view.ConsoleOutputHandler
import java.time.LocalDateTime

class Command3 (
    private val reader: ConsoleUserReader = ConsoleUserReader(),
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
): Command {
    override fun process(programInfo: ProgramInfo) {
        val upcomingSessions = programInfo.cinema.getUpcomingSessions()

        if (upcomingSessions.isEmpty()) {
            outputHandler.displayMessage("Доступных сеансов нет!")
            return
        }

        outputHandler.displaySessions(upcomingSessions)

        var attempts = 3
        while (attempts > 0) {
            outputHandler.displayMessage("Выберите сеанс для просмотра мест, введя соответствующий номер: ")
            val choice = reader.readInt()

            if (choice != null && choice in 1..upcomingSessions.size) {
                val chosenSession = upcomingSessions[choice - 1]
                outputHandler.displayHallOfChosenSession(chosenSession)
                break
            } else {
                outputHandler.displayMessage("Некорректный выбор сеанса!")
            }

            attempts--
        }

        if (attempts == 0) {
            outputHandler.displayMessage("Превышено количество попыток ввода. Операция отменена.")
        }
    }
}
