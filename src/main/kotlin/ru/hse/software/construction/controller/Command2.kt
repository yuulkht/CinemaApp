package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.repository.SessionRepositoryHandler
import ru.hse.software.construction.view.ConsoleOutputHandler
import java.time.LocalDateTime

class Command2 (
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

        var attempts = 3 // Количество попыток ввода
        while (attempts > 0) {
            outputHandler.displayMessage("Выберите сеанс для возврата билета, введя соответствующий номер: ")
            val choice = reader.readInt()

            if (choice != null && choice in 1..upcomingSessions.size) {
                val chosenSession = upcomingSessions[choice - 1]
                if (chosenSession.ifHallFullFree()) {
                    outputHandler.displayMessage("Все места свободны!")
                    return
                }
                outputHandler.displayHallOfChosenSession(chosenSession)
                outputHandler.displayMessage("Введите ряд:")
                val chosenRow = reader.readInt()
                outputHandler.displayMessage("Введите место:")
                val chosenCol = reader.readInt()

                if (chosenRow != null && chosenCol != null) {
                    if (chosenSession.getHall().cancelBookingSeat(chosenRow - 1, chosenCol - 1)) {

                        if (!SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                            outputHandler.displayError("не удалось сохранить изменения в файле")
                            return
                        }

                        outputHandler.displayMessage("Билет успешно возвращен!")
                        return
                    } else {
                        outputHandler.displayMessage("Не удалось вернуть билет. Убедитесь, что место было выкуплено.")
                    }
                } else {
                    outputHandler.displayMessage("Введите корректные значения для ряда и места!")
                }
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
