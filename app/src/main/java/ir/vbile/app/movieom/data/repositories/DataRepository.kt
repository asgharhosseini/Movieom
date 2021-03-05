package ir.vbile.app.movieom.data.repositories

import ir.vbile.app.movieom.data.model.genre.Genre
import ir.vbile.app.movieom.data.model.movie.Movie
import ir.vbile.app.movieom.data.model.movies.MoviesResponse
import ir.vbile.app.movieom.other.*
import retrofit2.Response

interface DataRepository {
 suspend  fun getGenres(): Response<List<Genre>>
    suspend fun getGenresMovies(id: Int): Response<MoviesResponse>
    suspend fun getMovie(id: Int): Response<Movie>
    suspend fun getSearchMovies(name: String): Response<MoviesResponse>
    suspend fun getAllMovies(page: Int): Response<MoviesResponse>
}