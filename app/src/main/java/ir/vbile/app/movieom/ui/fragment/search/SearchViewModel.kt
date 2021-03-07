package ir.vbile.app.movieom.ui.fragment.search

import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.base.*
import kotlinx.coroutines.*
import java.io.*

class SearchViewModel @ViewModelInject constructor(private val repository: MovieRepository) : BaseViewModel() {


    private val _SearchMovie = MutableLiveData<Resource<MoviesResponse>>()
    val SearchMovie: LiveData<Resource<MoviesResponse>> = _SearchMovie


    fun getGenresMovies(searchQuery: String) = viewModelScope.launch {

        handlerGetResponse(_SearchMovie, searchQuery)

    }

    suspend fun handlerGetResponse(responseGetter: MutableLiveData<Resource<MoviesResponse>>, searchQuery: String) {
        responseGetter.postValue(Resource.Loading())
        try {

            val responseBanner = repository.getSearchMovies(searchQuery)
            handleResponse(responseBanner, responseGetter)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> responseGetter.postValue(Resource.Error("Network Failure", null, 404))
                else -> responseGetter.postValue(Resource.Error("Conversion Error", null, 404))
            }

        }
    }


}