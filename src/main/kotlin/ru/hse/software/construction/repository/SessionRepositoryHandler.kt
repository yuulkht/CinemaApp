package ru.hse.software.construction.repository

import ru.hse.software.construction.model.Session

class SessionRepositoryHandler {
    fun isSavedSessions(sessions: MutableList<Session>): Boolean {
        return try {
            FileSessionRepository().saveSessions(sessions)
            true
        } catch (ex: FileSessionException) {
            false
        }
    }

    fun isGotSessionsSuccessful(): Boolean {
        return try {
            FileSessionRepository().getSessions()
            true
        } catch (ex: FileSessionException) {
            false
        }
    }
}
