package ir.vbile.app.movieom.data.model.movies

import android.annotation.SuppressLint
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class MoviesResponse(
    @SerializedName("data")
    var data: List<Data>,
    @SerializedName("metadata")
    var metadata: Metadata
) : Parcelable