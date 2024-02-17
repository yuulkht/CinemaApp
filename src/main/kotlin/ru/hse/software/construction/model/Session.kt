package ru.hse.software.construction.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.Duration
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
class Session @JsonCreator constructor(
    @JsonProperty("movie") private var movie: Movie,
    @JsonProperty("duration") @JsonFormat(shape = JsonFormat.Shape.STRING) private var duration: Duration,
    @JsonProperty("startTime") private var startTime: LocalDateTime,
    @JsonProperty("hall") private val hall: Hall = Hall(),
) {
    fun getMovie() : Movie {
        return movie
    }

    fun setMovie(value:Movie) {
        movie = value
    }

    fun getDuration() : Duration {
        return duration
    }

    fun setDuration(value: Duration) {
        duration = value
    }

    fun getStartTime() : LocalDateTime {
        return startTime
    }

    fun setStartTime(value: LocalDateTime) {
        startTime = value
    }

    fun getHall() : Hall {
        return hall
    }


    fun ifHallFullBooked() : Boolean {
        return hall.isFullBooked()
    }

    fun ifHallFullFree() : Boolean {
        return hall.isFullFree()
    }

    fun isOverlapping(otherStartTime : LocalDateTime, otherDuration : Duration): Boolean {
        val thisEndTime = this.startTime.plus(this.duration)
        val otherEndTime = otherStartTime.plus(otherDuration)

        val isOverlap = this.startTime.isBefore(otherEndTime) && thisEndTime.isAfter(otherStartTime)

        val isAdjacent = thisEndTime == otherStartTime || this.startTime == otherEndTime

        if (isAdjacent) {
            return false
        }

        return isOverlap
    }
    override fun toString(): String {
        return "Session = \n $movie \n Start Time = $startTime\n Duration = ${duration.toMinutes()} minutes"
    }

}