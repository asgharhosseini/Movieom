package ir.vbile.app.movieom.data.repositories

import ir.vbile.app.movieom.data.model.genre.*
import ir.vbile.app.movieom.data.model.movie.*
import ir.vbile.app.movieom.data.model.movies.*
import retrofit2.*

interface NetworkRepository {
    suspend fun getGenres(): Response<List<Genre>>
    suspend fun getGenresMovies(id: Int): Response<MoviesResponse>
    suspend fun getMovie(id: Int): Response<Movie>
    suspend fun getSearchMovies(name: String): Response<MoviesResponse>
    suspend fun getAllMovies(page: Int): Response<MoviesResponse>
}