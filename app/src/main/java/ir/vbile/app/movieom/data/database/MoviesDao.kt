package ir.vbile.app.movieom.data.database

import androidx.lifecycle.*
import androidx.room.*
import ir.vbile.app.movieom.data.model.movie.*


@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: MovieInDb)

    @Query("DELETE FROM tbl_movies_favorite WHERE id =:movieId")
    suspend fun deleteItem(movieId: Int)

    @Query("SELECT * FROM tbl_movies_favorite")
    fun getAllFavorite(): LiveData<List<MovieInDb>>

    @Query("SELECT EXISTS (SELECT 1 FROM tbl_movies_favorite  WHERE id = :id)")
    fun checkMovieIsFavorite(id: Int): LiveData<Boolean>


}