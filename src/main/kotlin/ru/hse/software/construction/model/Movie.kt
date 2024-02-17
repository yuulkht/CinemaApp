package ru.hse.software.construction.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class Movie (
    private var name : String = "",
    private var description : String = ""
) {
    fun getName() : String {
        return name
    }

    fun getDescription() : String {
        return description
    }
    fun changeName(value : String) {
        name = value
    }

    fun changeDescription(value : String) {
        description = value
    }

    override fun toString() : String {
        return "Movie. name = $name, description = $description"
    }

}