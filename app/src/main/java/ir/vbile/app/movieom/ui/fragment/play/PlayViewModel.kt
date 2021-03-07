package ir.vbile.app.movieom.ui.fragment.play

import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.genre.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.base.*
import kotlinx.coroutines.*
import java.io.*

class PlayViewModel @ViewModelInject constructor(private val repository: MovieRepository) : BaseViewModel() {
    private val _genre = MutableLiveData<Resource<List<Genre>>>()
    val genre: LiveData<Resource<List<Genre>>> = _genre

    init {
        getGenres()
    }

    fun getGenres() = viewModelScope.launch {
        _genre.postValue(Resource.Loading())
        try {
            val response = repository.getGenres()
            handleResponse(response, _genre)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> _genre.postValue(Resource.Error("Network Failure", null, 404))
                else -> _genre.postValue(Resource.Error("Conversion Error", null, 404))
            }

        }
    }
}