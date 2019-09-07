package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class GenreBaseData(
        val genres: List<GenreData>
): Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(GenreData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(genres)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GenreBaseData> {
        override fun createFromParcel(parcel: Parcel): GenreBaseData {
            return GenreBaseData(parcel)
        }

        override fun newArray(size: Int): Array<GenreBaseData?> {
            return arrayOfNulls(size)
        }
    }
}

data class GenreData(
    val id: Int,
    val name: String
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GenreData> {
        override fun createFromParcel(parcel: Parcel): GenreData {
            return GenreData(parcel)
        }

        override fun newArray(size: Int): Array<GenreData?> {
            return arrayOfNulls(size)
        }
    }
}