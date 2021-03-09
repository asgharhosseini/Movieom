package ir.vbile.app.movieom.data.repositories

import androidx.lifecycle.*
import ir.vbile.app.movieom.data.database.*
import ir.vbile.app.movieom.data.model.movie.*
import ir.vbile.app.movieom.data.network.*
import javax.inject.*

class MovieRepository @Inject constructor(
        private val api: ApiService,
        private val database: MoviesDao
) : NetworkRepository, DatabaseRepository {
    override suspend fun getGenres() = api.getGenres()

    override suspend fun getGenresMovies(id: Int) = api.getGenresMovies(id)

    override suspend fun getMovie(id: Int) = api.getMovie(id)

    override suspend fun getSearchMovies(name: String) = api.getSearchMovies(name)

    override suspend fun getAllMovies(page: Int) = api.getAllMovies(page)

    override suspend fun insertMovies(movies: MovieInDb) = database.insertMovies(movies)
    override suspend fun deleteItem(movieId: Int) = database.deleteItem(movieId)
    override fun getAllFavorite(): LiveData<List<MovieInDb>> = database.getAllFavorite()
    override fun checkMovieIsFavorite(id: Int): LiveData<Boolean> = database.checkMovieIsFavorite(id)

}