package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv")
data class TvData(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "poster_path") val poster_path: String?,
        @ColumnInfo(name = "popularity") val popularity: Float,
        @ColumnInfo(name = "backdrop_path") val backdrop_path: String?,
        @ColumnInfo(name = "vote_average") val vote_average: Float,
        @ColumnInfo(name = "overview") val overview: String,
        @ColumnInfo(name = "first_air_date") val first_air_date: String,
        @ColumnInfo(name = "original_language") val original_language: String,
        @ColumnInfo(name = "vote_count") val vote_count: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "original_name") val original_name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(poster_path)
        parcel.writeFloat(popularity)
        parcel.writeString(backdrop_path)
        parcel.writeFloat(vote_average)
        parcel.writeString(overview)
        parcel.writeString(first_air_date)
        parcel.writeString(original_language)
        parcel.writeInt(vote_count)
        parcel.writeString(name)
        parcel.writeString(original_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvData> {
        override fun createFromParcel(parcel: Parcel): TvData {
            return TvData(parcel)
        }

        override fun newArray(size: Int): Array<TvData?> {
            return arrayOfNulls(size)
        }
    }
}

data class TvBaseData(
        val results: List<TvData>,
        val page: Int,
        val total_results: Int,
        val total_pages: Int
)