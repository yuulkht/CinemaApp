package ru.hse.software.construction.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize

enum class SeatStatus {
    FREE,
    BOOKED,
    OCCUPIED
}

@JsonSerialize
class Hall (
    private var seats : MutableList<MutableList<SeatStatus>> = MutableList(10) { MutableList(10) { SeatStatus.FREE } }
) {
    fun getSeats() : MutableList<MutableList<SeatStatus>> {
        return seats
    }
    private fun isBooked(row : Int, col: Int) : Boolean {
        return seats[row][col] == SeatStatus.BOOKED
    }
    fun bookSeat(row: Int, col: Int): Boolean {
        return try {
            if (!isBooked(row, col)) {
                seats[row][col] = SeatStatus.BOOKED
                true
            } else {
                false
            }
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }

    fun cancelBookingSeat(row: Int, col: Int): Boolean {
        return try {
            if (isBooked(row, col)) {
                seats[row][col] = SeatStatus.FREE
                true
            } else {
                false
            }
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }


    fun markSeat(row: Int, col: Int): Boolean {
        return try {
            seats[row][col] = SeatStatus.OCCUPIED
            true
        } catch (ex: IndexOutOfBoundsException) {
            false
        }
    }

    @JsonIgnore
    fun isFullBooked(): Boolean {
        return seats.flatten().none { it == SeatStatus.FREE }
    }

    @JsonIgnore
    fun isFullFree(): Boolean {
        return seats.flatten().none { it == SeatStatus.BOOKED }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("    ")
        for (i in 1..10) {
            stringBuilder.append("$i ")
        }
        stringBuilder.appendLine()

        stringBuilder.append("   ")
        repeat(10) { stringBuilder.append("--") }
        stringBuilder.appendLine()

        for ((rowIndex, row) in seats.withIndex()) {
            stringBuilder.append(if (rowIndex != 9) "${rowIndex + 1} |" else "${rowIndex + 1}|")
            for (currentSeatStatus in row) {
                stringBuilder.append(when (currentSeatStatus) {
                    SeatStatus.BOOKED -> " X"
                    SeatStatus.FREE -> " 0"
                    SeatStatus.OCCUPIED -> " *"
                })
            }
            stringBuilder.appendLine()
        }

        stringBuilder.append("   ")
        repeat(10) { stringBuilder.append("--") }
        stringBuilder.appendLine()
        stringBuilder.appendLine("0 - свободные места")
        stringBuilder.appendLine("X - купленные места")
        stringBuilder.appendLine("* - занятые места")
        stringBuilder.appendLine()

        return stringBuilder.toString()
    }
}