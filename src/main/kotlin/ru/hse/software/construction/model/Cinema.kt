package ru.hse.software.construction.model

import java.time.LocalDateTime

interface CinemaModel {
    fun getUpcomingSessions(): List<Session>
    fun getMovies(): MutableList<Movie>
    fun getSessions(): MutableList<Session>
}

class Cinema (
    private var movies : MutableList<Movie> = mutableListOf(),
    private var sessions : MutableList<Session> = mutableListOf()
) : CinemaModel{

    override fun getMovies(): MutableList<Movie> {
        return movies
    }

    override fun getSessions(): MutableList<Session> {
        return sessions
    }

    override fun getUpcomingSessions(): List<Session> {
        val currentDateTime = LocalDateTime.now()
        return getSessions().filter { it.getStartTime().isAfter(currentDateTime) }
    }
}