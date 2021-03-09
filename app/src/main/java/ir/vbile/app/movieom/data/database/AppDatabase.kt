package ir.vbile.app.movieom.data.database

import androidx.room.*
import ir.vbile.app.movieom.data.model.movie.*

@Database(entities = [MovieInDb::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}