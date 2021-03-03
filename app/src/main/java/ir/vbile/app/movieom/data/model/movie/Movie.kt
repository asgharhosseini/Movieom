package ir.vbile.app.movieom.data.model.movie
import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Movie(
    @SerializedName("actors")
    var actors: String,
    @SerializedName("awards")
    var awards: String,
    @SerializedName("country")
    var country: String,
    @SerializedName("director")
    var director: String,
    @SerializedName("genres")
    var genres: List<String>,
    @SerializedName("id")
    var id: Int,
    @SerializedName("images")
    var images: List<String>,
    @SerializedName("imdb_id")
    var imdbId: String,
    @SerializedName("imdb_rating")
    var imdbRating: String,
    @SerializedName("imdb_votes")
    var imdbVotes: String,
    @SerializedName("metascore")
    var metascore: String,
    @SerializedName("plot")
    var plot: String,
    @SerializedName("poster")
    var poster: String,
    @SerializedName("rated")
    var rated: String,
    @SerializedName("released")
    var released: String,
    @SerializedName("runtime")
    var runtime: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("writer")
    var writer: String,
    @SerializedName("year")
    var year: String
) : Parcelable