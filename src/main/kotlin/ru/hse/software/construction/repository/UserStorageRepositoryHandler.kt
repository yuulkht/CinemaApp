package ru.hse.software.construction.repository

import ru.hse.software.construction.auth.UserStorage

class UserRepositoryHandler {
    fun isSavedUserStorage(userStorage: UserStorage) : Boolean{
        return try {
            FileUserStorageRepository().saveUserStorage(userStorage)
            true
        } catch (ex: FileUserStorageException) {
            false
        }
    }

    fun isGotUserStorageSuccessful() : Boolean{
        return try {
            FileUserStorageRepository().getUserStorage()
            true
        } catch (ex: FileUserStorageException) {
            false
        }
    }
}