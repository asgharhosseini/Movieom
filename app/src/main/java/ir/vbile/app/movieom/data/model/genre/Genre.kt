package ir.vbile.app.movieom.data.model.genre

import android.annotation.SuppressLint
import android.os.Parcelable

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Genre(

    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String
) : Parcelable