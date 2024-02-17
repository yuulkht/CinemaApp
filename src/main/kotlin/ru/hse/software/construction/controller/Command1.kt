package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.repository.SessionRepositoryHandler
import ru.hse.software.construction.view.ConsoleOutputHandler
import java.time.LocalDateTime

class Command1 (
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
            outputHandler.displayMessage("Выберите сеанс, введя соответствующий номер: ")
            val choice = reader.readInt()

            if (choice != null && choice in 1..upcomingSessions.size) {
                val chosenSession = upcomingSessions[choice - 1]
                if (chosenSession.ifHallFullBooked()) {
                    outputHandler.displayMessage("Все места раскуплены!")
                    return
                }
                outputHandler.displayHallOfChosenSession(chosenSession)
                outputHandler.displayMessage("Введите ряд:")
                val chosenRow = reader.readInt()
                outputHandler.displayMessage("Введите место:")
                val chosenCol = reader.readInt()

                if (chosenRow != null && chosenCol != null) {
                    if (chosenSession.getHall().bookSeat(chosenRow-1, chosenCol-1)) {

                        if (!SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                            outputHandler.displayError("не удалось сохранить изменения в файле")
                            return
                        }

                        outputHandler.displayMessage("Бронирование прошло успешно!")
                        return
                    } else {
                        outputHandler.displayMessage("Не удалось забронировать место (убедитесь, что оно свободно)")
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
