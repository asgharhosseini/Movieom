package ir.vbile.app.movieom.ui.fragment.home

import android.content.*
import android.net.*
import android.os.*
import android.view.*
import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.*
import io.reactivex.*
import ir.vbile.app.movieom.*
import ir.vbile.app.movieom.data.model.genre.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import kotlinx.coroutines.*
import retrofit2.*
import java.io.*
import javax.inject.*


class HomeViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository):ViewModel() {



    private val _genre=MutableLiveData<Resource<List<Genre>>>()
    val genre: LiveData<Resource<List<Genre>>> =_genre
    init {
        getGenres()
    }



    fun getGenres()=viewModelScope.launch {
        _genre.postValue(Resource.Loading())
        try {
            val response = movieRepository.getGenres()
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    _genre.postValue( Resource.Success( resultResponse))
                    }
                }else{
                    _genre.postValue(Resource.Error(response.message(),null,response.code()))
            }

        }catch (t:Throwable){
            when(t){
                is IOException->_genre.postValue(Resource.Error("Network Failure",null,404))
                else->_genre.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
    }



}