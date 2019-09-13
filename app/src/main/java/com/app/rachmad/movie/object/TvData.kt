package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class TvData(
        val poster_path: String?,
        val popularity: Float,
        val id: Int,
        val backdrop_path: String?,
        val vote_average: Float,
        val overview: String,
        val first_air_date: String,
        val origin_country: List<String>,
        val genre_ids: IntArray,
        val original_language: String,
        val vote_count: Int,
        val name: String,
        val original_name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.createStringArrayList()!!,
            parcel.createIntArray()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(poster_path)
        parcel.writeFloat(popularity)
        parcel.writeInt(id)
        parcel.writeString(backdrop_path)
        parcel.writeFloat(vote_average)
        parcel.writeString(overview)
        parcel.writeString(first_air_date)
        parcel.writeStringList(origin_country)
        parcel.writeIntArray(genre_ids)
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