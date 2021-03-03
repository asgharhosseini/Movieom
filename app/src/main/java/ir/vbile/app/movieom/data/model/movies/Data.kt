package ir.vbile.app.movieom.data.model.movies

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Data(
    @SerializedName("country")
    var country: String,
    @SerializedName("genres")
    var genres: List<String>,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: List<String>,
    @SerializedName("imdb_rating")
    var imdbRating: String,
    @SerializedName("poster")
    var poster: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("year")
    var year: String
) : Parcelable