package ir.vbile.app.movieom.di

import android.content.*
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
import ir.vbile.app.movieom.data.model.movies.*
import ir.vbile.app.movieom.data.network.*
import ir.vbile.app.movieom.data.repositories.*
import ir.vbile.app.movieom.other.*
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
    fun provideMovieRepository(apiService: ApiService)=MovieRepository(apiService)


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




}