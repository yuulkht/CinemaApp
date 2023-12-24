package ru.hse.software.construction.repository

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import ru.hse.software.construction.model.Movie
import java.io.*

class FileMovieException(
    filePath: String,
    override val message: String? = null,
    cause: Throwable? = null
) : RuntimeException("Problem with file $filePath\n$message", cause)

interface MovieRepository {
    fun saveMovies(movies: MutableList<Movie>)
    fun getMovies(): MutableList<Movie>
}

class FileMovieRepository(
    private val fileName: String = "movieRepository.json"
) : MovieRepository {

    private val file = File(fileName)

    override fun saveMovies(movies: MutableList<Movie>) {
        try {
            FileWriter(file).use { fileWriter ->
                ObjectMapper().writeValue(fileWriter, movies)
            }
        } catch (ex: Throwable) {
            throw FileMovieException(fileName, "Can't save movies", ex)
        }
    }

    override fun getMovies(): MutableList<Movie> {
        try {
            val movies = BufferedReader(FileReader(file)).use { bufferedReader ->
                bufferedReader.readText().let {
                    ObjectMapper().readValue(it, object : TypeReference<List<Movie>>() {})
                }
            }
            return movies.toMutableList()
        } catch (ex: Throwable) {
            throw FileMovieException(fileName, "Can't get movies", ex)
        }
    }
}
