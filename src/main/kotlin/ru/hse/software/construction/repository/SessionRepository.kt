package ru.hse.software.construction.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import ru.hse.software.construction.model.Session
import java.io.*

class FileSessionException(
    filePath: String,
    override val message: String? = null,
    cause: Throwable? = null
) : RuntimeException("Problem with file $filePath\n$message", cause)

interface SessionRepository {
    fun saveSessions(sessions: MutableList<Session>)
    fun getSessions(): MutableList<Session>
}

class FileSessionRepository(
    private val fileName: String = "sessionRepository.json"
) : SessionRepository {

    private val file = File(fileName)

    override fun saveSessions(sessions: MutableList<Session>) {
        try {
            FileWriter(file).use { fileWriter ->
                ObjectMapper().registerModule(JavaTimeModule()).writeValue(fileWriter, sessions)
            }
        } catch (ex: Throwable) {
            throw FileSessionException(fileName, "Can't save sessions", ex)
        }
    }

    override fun getSessions(): MutableList<Session> {
        try {
            val sessions = BufferedReader(FileReader(file)).use { bufferedReader ->
                bufferedReader.readText().let {
                    ObjectMapper().registerModule(JavaTimeModule()).readValue(it, object : TypeReference<List<Session>>() {})
                }
            }
            return sessions.toMutableList()
        } catch (ex: Throwable) {
            throw FileSessionException(fileName, "Can't get sessions", ex)
        }
    }
}