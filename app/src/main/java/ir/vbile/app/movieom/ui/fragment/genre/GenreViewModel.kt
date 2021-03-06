package ir.vbile.app.movieom.ui.fragment.genre

import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.base.*
import kotlinx.coroutines.*
import java.io.*

class GenreViewModel @ViewModelInject constructor(private val repository: MovieRepository) : BaseViewModel() {


    private val _genreMovie = MutableLiveData<Resource<MoviesResponse>>()
    val genreMovie: LiveData<Resource<MoviesResponse>> = _genreMovie


    fun getGenresMovies(genreId: Int) = viewModelScope.launch {

        handlerGetResponse(_genreMovie, genreId)

    }

    suspend fun handlerGetResponse(responseGetter: MutableLiveData<Resource<MoviesResponse>>, genreId: Int) {
        responseGetter.postValue(Resource.Loading())
        try {

            val responseBanner = repository.getGenresMovies(genreId)
            handleResponse(responseBanner, responseGetter)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> responseGetter.postValue(Resource.Error("Network Failure", null, 404))
                else -> responseGetter.postValue(Resource.Error("Conversion Error", null, 404))
            }

        }
    }
}