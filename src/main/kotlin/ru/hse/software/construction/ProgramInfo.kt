package ru.hse.software.construction

import ru.hse.software.construction.auth.AuthSession
import ru.hse.software.construction.auth.UserStorage
import ru.hse.software.construction.model.Cinema
import ru.hse.software.construction.repository.*

class ProgramInfo(
    var cinema: Cinema = Cinema(),
    var authSession: AuthSession = AuthSession(),
    var userStorage: UserStorage = UserStorage()
) {
    fun isFilledInfo() : Boolean {
        return if (MovieRepositoryHandler().isGotMoviesSuccessful() &&
            SessionRepositoryHandler().isGotSessionsSuccessful() &&
            UserRepositoryHandler().isGotUserStorageSuccessful()) {
            cinema = Cinema(FileMovieRepository().getMovies(), FileSessionRepository().getSessions())
            authSession = AuthSession()
            userStorage = FileUserStorageRepository().getUserStorage()
            true
        } else {
            false
        }
    }
}