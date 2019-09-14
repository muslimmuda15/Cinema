package com.app.rachmad.movie.sqlite.query

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.Status

@Dao
interface FavoriteQuery {
    @Query("select * from movie")
    fun getFavoriteMovieList(): DataSource.Factory<Int, MovieData>

    @Query("select * from tv")
    fun getFavoriteTvList(): DataSource.Factory<Int, TvData>

    @Insert(onConflict = REPLACE)
    fun insertMovieFavorite(movieData: MovieData)

    @Insert(onConflict = REPLACE)
    fun insertTvFavorite(tvData: TvData)

    @Query("select count(*) from movie")
    fun countAllFavoriteMovie(): Int

    @Query("select count(*) from tv")
    fun countAllFavoriteTv(): Int

    @Query("select count(*) from movie where id = :id")
    fun countFavoriteMovieLive(id: Int): LiveData<Int>

    @Query("select count(*) from tv where id = :id")
    fun countFavoriteTvLive(id: Int): LiveData<Int>

    @Delete
    fun deleteMovieFavorite(movieData: MovieData)

    @Delete
    fun deleteTvFavorite(tvData: TvData)

    @Query("select count(*) from movie where id = :id")
    fun countFavoriteMovie(id: Int): Int

    @Query("select count(*) from tv where id = :id")
    fun countFavoriteTv(id: Int): Int
}