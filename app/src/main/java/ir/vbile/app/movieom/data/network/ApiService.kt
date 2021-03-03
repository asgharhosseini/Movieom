package ir.vbile.app.movieom.data.network

import io.reactivex.Single
import ir.vbile.app.movieom.data.model.genre.Genre
import ir.vbile.app.movieom.data.model.movie.Movie
import ir.vbile.app.movieom.data.model.movies.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //http://moviesapi.ir/api/v1/genres
    @GET("genres")
    fun getGenres(): Response<List<Genre>>


    //http://moviesapi.ir/api/v1/genres/1/movies
    @GET("genres/{id}/movies")
    fun getGenresMovies(@Path("id") id: Int): Response<MoviesResponse>


    //http://moviesapi.ir/api/v1/movies/id
    @GET("movies/{id}")
    fun getMovie(@Path("id") id: Int): Response<Movie>


    //http://moviesapi.ir/api/v1/movies?q=name
    @GET("movies")
    fun getSearchMovies(@Query("q") name: String): Response<MoviesResponse>

    //http://moviesapi.ir/api/v1/movies?page=1
    @GET("movies")
    fun getAllMovies(@Query("page") id: Int): Response<MoviesResponse>
}