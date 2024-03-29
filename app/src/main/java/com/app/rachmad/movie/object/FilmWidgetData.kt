package com.app.rachmad.movie.`object`

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widget")
data class FilmWidgetData (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")    val id: Int,
        @ColumnInfo(name = "name")  val name: String,
        @ColumnInfo(name = "image") val image: String?
)