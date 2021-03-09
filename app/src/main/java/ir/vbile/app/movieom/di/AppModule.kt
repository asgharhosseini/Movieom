package ir.vbile.app.movieom.di

import android.content.*
import androidx.room.*
import com.bumptech.glide.*
import com.bumptech.glide.load.engine.*
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.*
import com.google.gson.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.android.qualifiers.*
import dagger.hilt.components.*
import ir.vbile.app.movieom.R
import ir.vbile.app.movieom.data.database.*
import ir.vbile.app.movieom.data.network.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.ui.adapter.*
import retrofit2.*
import retrofit2.converter.gson.*
import javax.inject.*


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlideInstance(
            @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .apply(RequestOptions().transform(CenterCrop(),RoundedCorners(16)))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
    )

    @Singleton
    @Provides
    fun provideMovieRepository(apiService: ApiService, database: MoviesDao) = MovieRepository(apiService, database)


    @Provides
    @Singleton
    internal fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun provideApiService(gson: Gson): ApiService =
            Retrofit.Builder()
                    .baseUrl(EndPoint.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(ApiService::class.java)

    //    @Singleton
    @Provides
    fun provideMovieAdapter(glide: RequestManager) = MoviesAdapter(glide)

    @Provides
    fun provideGenreMovieAdapter(glide: RequestManager) = GenreMoviesAdapter(glide)

    @Provides
    fun provideGenreMoviesAdapter(glide: RequestManager) = FavoriteMoviesAdapter(glide)

    @Provides
    fun provideGenrePlayAdapter() = GenresPlayAdapter()

    @Provides
    fun provideGenreAdapter() = GenreAdapter()

    @Provides
    fun provideGalleryImageAdapter(glide: RequestManager) = GalleryImageAdapter(glide)

    @Provides
    fun provideMoviesDao(appDatabase: AppDatabase): MoviesDao {
        return appDatabase.moviesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "favorite_user"
        ).allowMainThreadQueries()
                .build()
    }

}