package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class TvData(
        val original_name: String?,
        val id: Int,
        val name: String,
        val popularity: Float,
        val vote_count: Int,
        val vote_average: Float,
        val first_air_date: String,
        val poster_path: String,
        val genre_ids: List<String>,
        val original_language: String,
        val backdrop_path: String,
        val overview: String,
        val original_country: List<String>
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(original_name)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(popularity)
        parcel.writeInt(vote_count)
        parcel.writeFloat(vote_average)
        parcel.writeString(first_air_date)
        parcel.writeString(poster_path)
        parcel.writeStringList(genre_ids)
        parcel.writeString(original_language)
        parcel.writeString(backdrop_path)
        parcel.writeString(overview)
        parcel.writeStringList(original_country)
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