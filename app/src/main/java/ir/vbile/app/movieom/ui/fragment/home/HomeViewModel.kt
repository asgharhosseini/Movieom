package ir.vbile.app.movieom.ui.fragment.home


import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.genre.*
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import kotlinx.coroutines.*
import retrofit2.*
import java.io.*
import kotlin.random.*


class HomeViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository):ViewModel() {



    private val _genre=MutableLiveData<Resource<List<Genre>>>()
    val genre: LiveData<Resource<List<Genre>>> =_genre

    private val _genresMovies=MutableLiveData<Resource<MoviesResponse>>()
    val genresMovies: LiveData<Resource<MoviesResponse>> =_genresMovies

    init {
        getGenres()

    }



    fun getGenres()=viewModelScope.launch {
        _genre.postValue(Resource.Loading())
        try {
            val response = movieRepository.getGenres()
            handleResponse(response,_genre)

        }catch (t:Throwable){
            when(t){
                is IOException->_genre.postValue(Resource.Error("Network Failure",null,404))
                else->_genre.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
    }


    fun getGenresMovies(genreId: Int)=viewModelScope.launch {
        _genresMovies.postValue(Resource.Loading())
        try {
            val response = movieRepository.getGenresMovies(genreId)
            handleResponse(response,_genresMovies)

        }catch (t:Throwable){
            when(t){
                is IOException->_genresMovies.postValue(Resource.Error("Network Failure",null,404))
                else->_genresMovies.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
    }



    fun<T> handleResponse(response:Response<T>,data:MutableLiveData<Resource<T>>){
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                data.postValue( Resource.Success( resultResponse))
            }
        }else{
            data.postValue(Resource.Error(response.message(),null,response.code()))
        }
    }


}