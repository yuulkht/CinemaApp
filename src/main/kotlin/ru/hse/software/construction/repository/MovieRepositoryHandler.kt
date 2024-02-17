package ru.hse.software.construction.repository

import ru.hse.software.construction.model.Movie

class MovieRepositoryHandler {
    fun isSavedMovies(movies: MutableList<Movie>) : Boolean{
        return try {
            FileMovieRepository().saveMovies(movies)
            true
        } catch (ex: FileMovieException) {
            false
        }
    }

    fun isGotMoviesSuccessful() : Boolean{
        return try {
            FileMovieRepository().getMovies()
            true
        } catch (ex: FileMovieException) {
            false
        }
    }
}