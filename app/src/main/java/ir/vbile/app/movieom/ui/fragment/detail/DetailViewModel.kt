package ir.vbile.app.movieom.ui.fragment.detail

import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.movie.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.base.*
import kotlinx.coroutines.*
import java.io.*

class DetailViewModel @ViewModelInject constructor(private val repository: MovieRepository) : BaseViewModel() {

    private val _movie = MutableLiveData<Resource<Movie>>()
    val movie: LiveData<Resource<Movie>> = _movie


    fun getMovie(movieId: Int) = viewModelScope.launch {
        _movie.postValue(Resource.Loading())
        try {
            val response = repository.getMovie(movieId)
            handleResponse(response, _movie)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _movie.postValue(Resource.Error("Network Failure", null, 404))
                else -> _movie.postValue(Resource.Error("Conversion Error", null, 404))
            }

        }
    }


    fun addFavoriteMovie(movie: MovieInDb) = viewModelScope.launch {
        repository.insertMovies(movie)
    }

    fun deleteItem(movieId: Int) = viewModelScope.launch {
        repository.deleteItem(movieId)
    }

    fun checkMovieIsFavorite(movieId: Int): LiveData<Boolean> = repository.checkMovieIsFavorite(movieId)


}