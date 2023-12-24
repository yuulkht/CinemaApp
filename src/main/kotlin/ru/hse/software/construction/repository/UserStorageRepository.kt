package ru.hse.software.construction.repository

import com.fasterxml.jackson.databind.ObjectMapper
import ru.hse.software.construction.auth.UserStorage
import java.io.File
import java.io.FileWriter


class FileUserStorageException(
    filePath: String,
    override val message: String? = null,
    cause: Throwable? = null
) : RuntimeException("Problem with file $filePath\n$message", cause)

interface UserStorageRepository {
    fun saveUserStorage(userStorage: UserStorage)
    fun getUserStorage(): UserStorage
}

class FileUserStorageRepository(
    private val fileName: String = "userStorageRepository.json"
) : UserStorageRepository {

    private val file = File(fileName)

    override fun saveUserStorage(userStorage: UserStorage) {
        try {
            FileWriter(file, false).use { fileWriter ->
                fileWriter.write(ObjectMapper().writeValueAsString(userStorage) + "\n")
                fileWriter.flush()
            }
        } catch (ex: Throwable) {
            throw FileUserStorageException(fileName, "Can't save userStorage", ex)
        }
    }

    override fun getUserStorage(): UserStorage {
        try {
            return ObjectMapper().readValue(file.useLines { it.joinToString("\n") }, UserStorage::class.java)
        } catch (ex: Throwable) {
            throw FileUserStorageException(fileName, "Can't get users", ex)
        }
    }
}