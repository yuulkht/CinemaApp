package ru.hse.software.construction.view

import ru.hse.software.construction.model.Hall
import ru.hse.software.construction.model.Movie
import ru.hse.software.construction.model.Session

interface OutputHandler {
    fun displayCommands()
    fun displayAuthCommands()
    fun displayMessage(message: String)
    fun displayError(message: String)
    fun displaySessions(sessions : List<Session>)
    fun displayMovies(movies: List<Movie>)
    fun displayHallOfChosenSession(session: Session)
    fun displayHall(hall: Hall)
    fun displaySession(session: Session)
}

// Реализация интерфейса для вывода в консоль
class ConsoleOutputHandler : OutputHandler {
    override fun displayCommands() {
        println("Список команд: ")
        println()

        println("1 - Зафиксировать продажу билета")
        println("2 - Вернуть билет")
        println("3 - Выбрать сеанс и посмотреть свободные места")
        println("4 - Редактировать информацию о фильмах в прокате")
        println("5 - Редактировать информацию о сеансах")
        println("6 - Отметить занятые места в зале")
        println("logout - Выйти из системы")

    }

    override fun displayAuthCommands() {
        println()
        println("Для использования приложения необходим вход в систему!")
        println()
        println("register - Зарегистрироваться")
        println("login - Войти")
    }

    override fun displayMessage(message: String) {
        println(message)
    }

    override fun displayError(message: String) {
        println("Ошибка: $message")
    }

    override fun displaySessions(sessions : List<Session>) {
        println("Доступные сеансы: ")
        for ((index, session) in sessions.withIndex()) {
            println("№${index + 1}")
            println(session)
        }
    }

    override fun displayMovies(movies: List<Movie>) {
        println("Доступные фильмы:")
        for ((index, movie) in movies.withIndex()) {
            println("№${index + 1}")
            println(movie)
        }
    }

    override fun displayHallOfChosenSession(session: Session) {
        displayMessage("Зал выбранного сеанса: ")
        println(session.getHall().toString())
    }

    override fun displayHall(hall: Hall) {
        println(hall)
    }

    override fun displaySession(session: Session) {
        println(session)
    }
}