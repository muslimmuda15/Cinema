package com.app.rachmad.movie.sqlite.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteTable(
        @PrimaryKey
        @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "title") val name: String,
        @ColumnInfo(name = "overview") val overview: String,
        @ColumnInfo(name = "poster") val poster_path: String?,
        @ColumnInfo(name = "backdrop") val backdrop_path: String?,
        @ColumnInfo(name = "vote") val vote_average: Float,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "type") val type: Int
)