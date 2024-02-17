package ru.hse.software.construction.reader

import java.time.Duration
import java.time.LocalDateTime

interface UserReader {
    fun readInt() : Int?
    fun readString() : String?
    fun readAuthData() : String
    fun readDuration() : Duration?
    fun readDate() : LocalDateTime?
}
class ConsoleUserReader : UserReader{
    override fun readInt(): Int? {
        return readlnOrNull()?.toIntOrNull()
    }

    override fun readString(): String? {
        return readlnOrNull()
    }

    override fun readAuthData(): String {
        return readln()
    }

    override fun readDuration(): Duration? {
        return readlnOrNull()?.toLongOrNull()?.let { Duration.ofMinutes(it) }
    }
    override fun readDate(): LocalDateTime {
        return LocalDateTime.parse(readln())
    }
}