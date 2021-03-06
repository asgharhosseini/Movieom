package ir.vbile.app.movieom.ui.fragment.home


import androidx.hilt.lifecycle.*
import androidx.lifecycle.*
import ir.vbile.app.movieom.data.model.genre.*
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
import ir.vbile.app.movieom.ui.base.*
import kotlinx.coroutines.*
import java.io.*
import kotlin.random.*


class HomeViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) : BaseViewModel() {
    var banner = Random.nextInt(1, 21)
    var top = Random.nextInt(1, 21)
    var center = Random.nextInt(1, 21)
    var down = Random.nextInt(1, 21)


    private val _genre = MutableLiveData<Resource<List<Genre>>>()
    val genre: LiveData<Resource<List<Genre>>> = _genre

    private val _genresMoviesTop = MutableLiveData<Resource<MoviesResponse>>()
    val genresMoviesTop: LiveData<Resource<MoviesResponse>> =_genresMoviesTop

    private val _genresMoviesCenter=MutableLiveData<Resource<MoviesResponse>>()
    val genresMoviesCenter: LiveData<Resource<MoviesResponse>> =_genresMoviesCenter

    private val _genresMoviesDown=MutableLiveData<Resource<MoviesResponse>>()
    val genresMoviesDown: LiveData<Resource<MoviesResponse>> =_genresMoviesDown

    private val _genresMoviesBanner=MutableLiveData<Resource<MoviesResponse>>()
    val genresMoviesBanner: LiveData<Resource<MoviesResponse>> =_genresMoviesBanner

    init {
        getGenres()
        getGenresMovies(banner,top,center,down)
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


    suspend fun handlerGetResponse(responseGetter: MutableLiveData<Resource<MoviesResponse>>, genreId:Int) {
        responseGetter.postValue(Resource.Loading())
        try {

            val responseBanner = movieRepository.getGenresMovies(genreId)
            handleResponse(responseBanner, responseGetter)

        } catch (t: Throwable) {
            when (t) {
                is IOException -> responseGetter.postValue(Resource.Error("Network Failure", null, 404))
                else -> responseGetter.postValue(Resource.Error("Conversion Error", null, 404))
            }

        }
    }

    fun getGenresMovies(genreIdBanner: Int, genreIdTop: Int, genreIdCenter: Int, genreIdDown: Int) = viewModelScope.launch {

        handlerGetResponse(_genresMoviesBanner, genreIdBanner)
        handlerGetResponse(_genresMoviesTop, genreIdTop)
        handlerGetResponse(_genresMoviesCenter, genreIdCenter)
        handlerGetResponse(_genresMoviesDown, genreIdDown)
    }


}