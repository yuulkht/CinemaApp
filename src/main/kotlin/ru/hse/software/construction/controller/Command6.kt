package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.view.ConsoleOutputHandler
import java.time.LocalDateTime

class Command6 (
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
                val hall = chosenSession.getHall()
                outputHandler.displayHallOfChosenSession(chosenSession)

                var addMoreSeats = true

                while (addMoreSeats) {
                    outputHandler.displayMessage("Введите ряд:")
                    val chosenRow = reader.readInt()

                    outputHandler.displayMessage("Введите место:")
                    val chosenCol = reader.readInt()

                    if (chosenRow != null && chosenCol != null) {
                        if (hall.markSeat(chosenRow - 1, chosenCol - 1)) {
                            outputHandler.displayMessage("Место успешно отмечено как занятое!")
                            outputHandler.displayHall(hall)
                        } else {
                            outputHandler.displayMessage("Не удалось отметить место.")
                        }
                    } else {
                        outputHandler.displayMessage("Введите корректные значения для ряда и места!")
                    }

                    outputHandler.displayMessage("Если хотите отметить еще место, введите Y")
                    val addMoreChoice = reader.readString()

                    if (addMoreChoice != "Y") {
                        addMoreSeats = false
                    }
                }
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

