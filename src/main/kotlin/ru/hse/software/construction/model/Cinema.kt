package ru.hse.software.construction.model

import java.time.LocalDateTime

interface CinemaModel {
    fun getUpcomingSessions(): List<Session>
}

class Cinema (
    private var movies : MutableList<Movie> = mutableListOf(),
    private var sessions : MutableList<Session> = mutableListOf()
) : CinemaModel{

    fun getMovies(): MutableList<Movie> {
        return movies
    }

    fun getSessions(): MutableList<Session> {
        return sessions
    }

    override fun getUpcomingSessions(): List<Session> {
        val currentDateTime = LocalDateTime.now()
        return getSessions().filter { it.getStartTime().isAfter(currentDateTime) }
    }
}