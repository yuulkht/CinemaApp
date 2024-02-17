package ru.hse.software.construction.controller

import ru.hse.software.construction.ProgramInfo
import ru.hse.software.construction.model.Movie
import ru.hse.software.construction.reader.ConsoleUserReader
import ru.hse.software.construction.repository.FileMovieRepository
import ru.hse.software.construction.repository.MovieRepositoryHandler
import ru.hse.software.construction.repository.SessionRepositoryHandler
import ru.hse.software.construction.view.ConsoleOutputHandler

class Command4 (
    private val reader: ConsoleUserReader = ConsoleUserReader(),
    private val outputHandler: ConsoleOutputHandler = ConsoleOutputHandler()
): Command {
    override fun process(programInfo: ProgramInfo) {
        outputHandler.displayMessage("Выберите действие:")
        outputHandler.displayMessage("1. Редактировать данные о фильме")
        outputHandler.displayMessage("2. Добавить новый фильм")
        outputHandler.displayMessage("3. Удалить фильм")
        val choice = reader.readInt()

        when (choice) {
            1 -> editMovie(programInfo)
            2 -> addMovie(programInfo)
            3 -> deleteMovie(programInfo)
            else -> outputHandler.displayMessage("Некорректный выбор.")
        }
    }

    private fun editMovie(programInfo: ProgramInfo) {
        val movies = programInfo.cinema.getMovies()
        outputHandler.displayMovies(movies)

        outputHandler.displayMessage("Выберите фильм для редактирования, введя соответствующий номер: ")
        val movieChoice = reader.readInt()

        if (movieChoice != null && movieChoice in 1..movies.size) {
            val chosenMovie = movies[movieChoice - 1]
            outputHandler.displayMessage("Введите новое название фильма:")
            val newName = reader.readString()
            outputHandler.displayMessage("Введите новое описание фильма:")
            val newDescription = reader.readString()

            if (!newName.isNullOrBlank()) {
                val oldName = chosenMovie.getName()
                chosenMovie.changeName(newName)

                // Обновить сеансы, связанные с этим фильмом
                programInfo.cinema.getSessions().forEach { session ->
                    if (session.getMovie().getName() == oldName) {
                        session.getMovie().changeName(newName)
                    }
                }
            } else {
                outputHandler.displayMessage("Название фильма не может быть пустым")
                return
            }

            if (!newDescription.isNullOrBlank()) {
                chosenMovie.changeDescription(newDescription)

                // Обновить сеансы, связанные с этим фильмом
                programInfo.cinema.getSessions().forEach { session ->
                    if (session.getMovie().getName() == chosenMovie.getName()) {
                        session.getMovie().changeDescription(newDescription)
                    }
                }
            }

            if (!MovieRepositoryHandler().isSavedMovies(movies) &&
                !SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                outputHandler.displayError("Не удалось сохранить изменения в файле")
                return
            }

            outputHandler.displayMessage("Данные о фильме и связанных сеансах успешно отредактированы.")
        } else {
            outputHandler.displayMessage("Некорректный выбор фильма.")
        }
    }

    private fun addMovie(programInfo: ProgramInfo) {
        outputHandler.displayMessage("Введите название нового фильма:")
        val newName = reader.readString()

        if (newName.isNullOrBlank()) {
            outputHandler.displayMessage("Название фильма не может быть пустым.")
            return
        }

        if (programInfo.cinema.getMovies().any { it.getName().equals(newName, ignoreCase = true) }) {
            outputHandler.displayMessage("Фильм с таким названием уже существует.")
            return
        }

        outputHandler.displayMessage("Введите описание нового фильма:")
        val newDescription = reader.readString()

        val newMovie = Movie(newName, newDescription ?: "")

        programInfo.cinema.getMovies().add(newMovie)
        if (!MovieRepositoryHandler().isSavedMovies(programInfo.cinema.getMovies())) {
            outputHandler.displayError("не удалось сохранить изменения в файле")
            return
        }

        outputHandler.displayMessage("Новый фильм успешно добавлен.")
    }

    private fun deleteMovie(programInfo: ProgramInfo) {
        val movies = programInfo.cinema.getMovies()
        outputHandler.displayMovies(movies)

        outputHandler.displayMessage("Выберите фильм для удаления, введя соответствующий номер: ")
        val movieChoice = reader.readInt()

        if (movieChoice != null && movieChoice in 1..movies.size) {
            val chosenMovie = movies[movieChoice - 1]

            programInfo.cinema.getSessions().removeIf { session ->
                session.getMovie() == chosenMovie
            }

            programInfo.cinema.getMovies().remove(chosenMovie)
            if (MovieRepositoryHandler().isSavedMovies(programInfo.cinema.getMovies()) &&
                SessionRepositoryHandler().isSavedSessions(programInfo.cinema.getSessions())) {
                outputHandler.displayMessage("Фильм и связанные с ним сеансы успешно удалены.")
            } else {
                outputHandler.displayError("Не удалось сохранить изменения в файле")
                return
            }
        } else {
            outputHandler.displayMessage("Некорректный выбор фильма.")
        }
    }
}
