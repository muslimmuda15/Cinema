package com.app.rachmad.movie.sqlite.query

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.sqlite.table.FavoriteTable

@Dao
interface FavoriteQuery {
    @Query("select * from favorite where type = ${Status.MOVIE}")
    fun getFavoriteMovieList(): List<FavoriteTable>

    @Query("select * from favorite where type = ${Status.TV}")
    fun getFavoriteTvList(): List<FavoriteTable>

    @Insert(onConflict = REPLACE)
    fun insertFavorite(favoriteTable: FavoriteTable)

    @Query("select count(*) from favorite where id = :id and type = '${Status.MOVIE}'")
    fun countFavoriteMovieLive(id: Int): LiveData<Int>

    @Query("select count(*) from favorite where id = :id and type = '${Status.TV}'")
    fun countFavoriteTvLive(id: Int): LiveData<Int>

    @Delete
    fun deleteFavorite(favoriteTable: FavoriteTable)

    @Query("select count(*) from favorite where id = :id and type = '${Status.MOVIE}'")
    fun countFavoriteMovie(id: Int): Int

    @Query("select count(*) from favorite where id = :id and type = '${Status.TV}'")
    fun countFavoriteTv(id: Int): Int
}