package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.model.Movie
import ru.hse.software.construction.model.Session
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.repository.FileSessionRepository
import ru.hse.software.construction.repository.MovieRepositoryHandler
import ru.hse.software.construction.repository.SessionRepositoryHandler
import ru.hse.software.construction.view.ConsoleOutputHandler
import java.time.Duration
import java.time.LocalDateTime

class Command5 (
    private val reader: ConsoleUserReader = ConsoleUserReader(),
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
): Command {
    override fun process(programInfo: ProgramInfo) {
        outputHandler.displayMessage("Выберите действие:")
        outputHandler.displayMessage("1. Редактировать сеанс")
        outputHandler.displayMessage("2. Добавить новый сеанс")
        outputHandler.displayMessage("3. Удалить сеанс")
        val choice = reader.readInt()

        when (choice) {
            1 -> editSession(programInfo)
            2 -> addSession(programInfo)
            3 -> deleteSession(programInfo)
            else -> outputHandler.displayMessage("Некорректный выбор.")
        }
    }

    private fun editSession(programInfo: ProgramInfo) {
        val sessions = programInfo.cinema.getSessions()

        outputHandler.displaySessions(sessions)

        outputHandler.displayMessage("Выберите сеанс для редактирования, введя соответствующий номер: ")
        val sessionChoice = reader.readInt()

        if (sessionChoice != null && sessionChoice in 1..sessions.size) {
            val chosenSession = sessions[sessionChoice - 1]
            val hall = chosenSession.getHall()

            outputHandler.displayMessage("Текущая информация о сеансе:")
            outputHandler.displaySession(chosenSession)

            outputHandler.displayMessage("Выберите новый фильм для сеанса:")
            val newMovie = selectMovie(programInfo)

            var newStartTime : LocalDateTime? = null

            try {
                outputHandler.displayMessage("Введите новое время начала сеанса (гггг-мм-ддТчч:мм): ")
                newStartTime = reader.readDate()
            } catch (ex: Throwable){
                outputHandler.displayMessage("Вы ввели некорректное время начала сеанса!")
                return
            }

            outputHandler.displayMessage("Введите новую длительность сеанса (в минутах): ")
            val newDuration = reader.readDuration()

            if (newDuration != null) {
                if (newDuration <= Duration.ofMinutes(0)) {
                    outputHandler.displayMessage("Длительность не может быть неположительной")
                    return
                }

                val isOverlap = sessions.any { session ->
                    session != chosenSession && session.isOverlapping(newStartTime, newDuration)
                }

                if (!isOverlap) {
                    chosenSession.setMovie(newMovie)
                    chosenSession.setDuration(newDuration)
                    chosenSession.setStartTime(newStartTime)

                    if (!SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                        outputHandler.displayError("не удалось сохранить изменения в файле")
                        return
                    }

                    outputHandler.displayMessage("Сеанс успешно отредактирован.")
                } else {
                    outputHandler.displayMessage("Новый сеанс пересекается с другими сеансами. Редактирование отменено.")
                }
            } else {
                outputHandler.displayMessage("Некорректная длительность сеанса. Редактирование отменено.")
            }
        } else {
            outputHandler.displayMessage("Некорректный выбор сеанса.")
        }
    }

    private fun selectMovie(programInfo: ProgramInfo): Movie {
        val movies = programInfo.cinema.getMovies()
        outputHandler.displayMovies(movies)

        outputHandler.displayMessage("Выберите фильм, введя соответствующий номер:")
        val movieChoice = reader.readInt()

        return if (movieChoice != null && movieChoice in 1..movies.size) {
            movies[movieChoice - 1]
        } else {
            outputHandler.displayMessage("Некорректный выбор фильма. Используется первый фильм.")
            movies.first()
        }
    }

    private fun addSession(programInfo: ProgramInfo) {
        val movies = programInfo.cinema.getMovies()

        outputHandler.displayMovies(movies)

        outputHandler.displayMessage("Выберите фильм для нового сеанса, введя соответствующий номер:")
        val movieChoice = reader.readInt()

        val selectedMovie = if (movieChoice != null && movieChoice in 1..movies.size) {
            movies[movieChoice - 1]
        } else {
            outputHandler.displayMessage("Некорректный выбор фильма")
            return
        }

        var startTime: LocalDateTime? = null

        try {
            outputHandler.displayMessage("Введите время начала нового сеанса (гггг-мм-ддТчч:мм): ")
            startTime = reader.readDate()
        } catch (ex: Throwable) {
            outputHandler.displayMessage("Вы ввели некорректное время начала сеанса!")
            return
        }

        outputHandler.displayMessage("Введите длительность нового сеанса (в минутах): ")
        val duration = reader.readDuration()

        if (duration != null) {
            if (duration <= Duration.ofMinutes(0)) {
                outputHandler.displayMessage("Длительность не может быть неположительной")
                return
            }
            val isOverlap = programInfo.cinema.getSessions().any {
                it.isOverlapping(startTime, duration)
            }

            if (!isOverlap) {
                val newSession = Session(selectedMovie, duration, startTime)

                programInfo.cinema.getSessions().add(newSession)

                if (!SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                    outputHandler.displayError("не удалось сохранить изменения в файле")
                    return
                }

                outputHandler.displayMessage("Новый сеанс успешно добавлен.")
            } else {
                outputHandler.displayMessage("Новый сеанс пересекается с другими сеансами. Добавление отменено.")
            }
        } else {
            outputHandler.displayMessage("Некорректная длительность сеанса.")
        }
    }

    private fun deleteSession(programInfo: ProgramInfo) {
        val sessions = programInfo.cinema.getSessions()

        outputHandler.displaySessions(sessions)

        outputHandler.displayMessage("Выберите сеанс для удаления, введя соответствующий номер: ")
        val sessionChoice = reader.readInt()

        if (sessionChoice != null && sessionChoice in 1..sessions.size) {
            val chosenSession = sessions[sessionChoice - 1]

            programInfo.cinema.getSessions().remove(chosenSession)

            if (!SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                outputHandler.displayError("не удалось сохранить изменения в файле")
                return
            }

            outputHandler.displayMessage("Сеанс успешно удален.")
        } else {
            outputHandler.displayMessage("Некорректный выбор сеанса.")
        }
    }

}
