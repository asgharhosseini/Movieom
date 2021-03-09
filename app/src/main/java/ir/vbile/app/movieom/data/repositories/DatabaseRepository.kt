package ir.vbile.app.movieom.data.repositories

import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.movie.*

interface DatabaseRepository {

    suspend fun insertMovies(movies: MovieInDb)
    suspend fun deleteItem(movieId: Int)
    fun getAllFavorite(): LiveData<List<MovieInDb>>
    fun checkMovieIsFavorite(id: Int): LiveData<Boolean>


}