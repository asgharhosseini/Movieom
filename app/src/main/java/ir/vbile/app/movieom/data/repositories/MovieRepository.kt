package ir.vbile.app.movieom.data.repositories

import ir.vbile.app.movieom.data.model.genre.Genre
import ir.vbile.app.movieom.data.model.movie.Movie
import ir.vbile.app.movieom.data.model.movies.MoviesResponse
import ir.vbile.app.movieom.data.network.ApiService
import ir.vbile.app.movieom.other.*
import retrofit2.Response
import javax.inject.Inject

 class MovieRepository @Inject constructor(private val api:ApiService):DataRepository{
      override suspend fun getGenres() = api.getGenres()

     override suspend fun getGenresMovies(id: Int)=api.getGenresMovies(id)

     override suspend fun getMovie(id: Int) =api.getMovie(id)

     override suspend fun getSearchMovies(name: String)=api.getSearchMovies(name)

     override suspend fun getAllMovies(page: Int)=  api.getAllMovies(page)


 }