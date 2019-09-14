package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieData(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "poster_path") val poster_path: String?,
        @ColumnInfo(name = "adult") val adult: Boolean,
        @ColumnInfo(name = "overview") val overview: String,
        @ColumnInfo(name = "release_date") val release_date: String,
        @ColumnInfo(name = "original_title") val original_title: String,
        @ColumnInfo(name = "original_language") val original_language: String,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "backdrop_path") val backdrop_path: String?,
        @ColumnInfo(name = "popularity") val popularity: Float,
        @ColumnInfo(name = "vote_count") val vote_count: Int,
        @ColumnInfo(name = "video") val video: Boolean,
        @ColumnInfo(name = "vote_average") val vote_average: Float
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(poster_path)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeString(original_title)
        parcel.writeString(original_language)
        parcel.writeString(title)
        parcel.writeString(backdrop_path)
        parcel.writeFloat(popularity)
        parcel.writeInt(vote_count)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeFloat(vote_average)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieData> {
        override fun createFromParcel(parcel: Parcel): MovieData {
            return MovieData(parcel)
        }

        override fun newArray(size: Int): Array<MovieData?> {
            return arrayOfNulls(size)
        }
    }
}

data class MovieBaseData(
        val results: List<MovieData>,
        val page: Int,
        val total_results: Int,
        val total_pages: Int
)