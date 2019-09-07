package com.app.rachmad.movie.`object`

import android.os.Parcel
import android.os.Parcelable

data class ErrorData(
    val status_code: Int,
    val status_message: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status_code)
        parcel.writeString(status_message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ErrorData> {
        override fun createFromParcel(parcel: Parcel): ErrorData {
            return ErrorData(parcel)
        }

        override fun newArray(size: Int): Array<ErrorData?> {
            return arrayOfNulls(size)
        }
    }
}