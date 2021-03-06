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
    var banner = Random.nextInt(1, 21)
    var top = Random.nextInt(1, 21)
    var center = Random.nextInt(1, 21)
    var down = Random.nextInt(1, 21)


    private val _genre=MutableLiveData<Resource<List<Genre>>>()
    val genre: LiveData<Resource<List<Genre>>> =_genre

    private val _genresMoviesTop=MutableLiveData<Resource<MoviesResponse>>()
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


    fun getGenresMovies(genreIdBanner: Int,genreIdTop: Int,genreIdCenter: Int,genreIdDown: Int)=viewModelScope.launch {
        _genresMoviesBanner.postValue(Resource.Loading())


        try {

            val responseBanner = movieRepository.getGenresMovies(genreIdBanner)
            handleResponse(responseBanner,_genresMoviesBanner)

        }catch (t:Throwable){
            when(t){
                is IOException->_genresMoviesBanner.postValue(Resource.Error("Network Failure",null,404))
                else->_genresMoviesBanner.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
        _genresMoviesTop.postValue(Resource.Loading())


        try {

            val responseTop = movieRepository.getGenresMovies(genreIdTop)
            handleResponse(responseTop,_genresMoviesTop)

        }catch (t:Throwable){
            when(t){
                is IOException->_genresMoviesTop.postValue(Resource.Error("Network Failure",null,404))
                else->_genresMoviesTop.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
        _genresMoviesCenter.postValue(Resource.Loading())

        try {

            val responseCenter = movieRepository.getGenresMovies(genreIdCenter)
            handleResponse(responseCenter,_genresMoviesCenter)


        }catch (t:Throwable){
            when(t){
                is IOException->_genresMoviesCenter.postValue(Resource.Error("Network Failure",null,404))
                else->_genresMoviesCenter.postValue(Resource.Error("Conversion Error",null,404))
            }

        }
        _genresMoviesDown.postValue(Resource.Loading())
        try {


            val responseDown = movieRepository.getGenresMovies(genreIdDown)
            handleResponse(responseDown,_genresMoviesDown)

        }catch (t:Throwable){
            when(t){
                is IOException->_genresMoviesDown.postValue(Resource.Error("Network Failure",null,404))
                else->_genresMoviesDown.postValue(Resource.Error("Conversion Error",null,404))
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