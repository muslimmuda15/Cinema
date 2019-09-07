package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class MovieData(
        val popularity: Float,
        val vote_count: Int,
        val video: Boolean,
        val poster_path: String,
        val id: Int,
        val adult: Boolean,
        val backdrop_path: String,
        val original_language: String,
        val original_title: String?,
        val genre_ids: List<String>,
        val title: String,
        val vote_average: Float,
        val overview: String,
        val release_date: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(popularity)
        parcel.writeInt(vote_count)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeString(poster_path)
        parcel.writeInt(id)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeString(backdrop_path)
        parcel.writeString(original_language)
        parcel.writeString(original_title)
        parcel.writeStringList(genre_ids)
        parcel.writeString(title)
        parcel.writeFloat(vote_average)
        parcel.writeString(overview)
        parcel.writeString(release_date)
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