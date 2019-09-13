package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class MovieData(
        val poster_path: String?,
        val adult: Boolean,
        val overview: String,
        val release_date: String,
        val genre_ids: IntArray,
        val id: Int,
        val original_title: String,
        val original_language: String,
        val title: String,
        val backdrop_path: String?,
        val popularity: Float,
        val vote_count: Int,
        val video: Boolean,
        val vote_average: Float
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createIntArray()!!,
            parcel.readInt(),
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
        parcel.writeString(poster_path)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(overview)
        parcel.writeString(release_date)
        parcel.writeIntArray(genre_ids)
        parcel.writeInt(id)
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