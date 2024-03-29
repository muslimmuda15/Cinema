package com.app.rachmad.movie.sqlite.query

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.Status

@Dao
interface FavoriteQuery {
    @Query("select * from movie")
    fun getFavoriteMovieList(): DataSource.Factory<Int, MovieData>

    @Query("select * from movie")
    fun getFavoriteMovieStackList(): LiveData<List<MovieData>>

    @Query("select * from movie")
    fun getFavoriteMovie(): List<MovieData>

    @Query("select * from tv")
    fun getFavoriteTv(): List<TvData>

    @Query("select * from movie")
    fun getFavoriteMovieCursor(): Cursor

    @Query("select * from tv")
    fun getFavoriteTvCursor(): Cursor

    @Query("select * from movie where id = :id")
    fun getFavoriteMovie(id: Int): Cursor

    @Query("select * from tv where id = :id")
    fun getFavoriteTv(id: Int): Cursor

    @Query("select * from tv")
    fun getFavoriteTvList(): DataSource.Factory<Int, TvData>

    @Query("select * from tv")
    fun getFavoriteTvStackList(): LiveData<List<TvData>>

    @Insert(onConflict = REPLACE)
    fun insertMovieFavorite(movieData: MovieData)

    @Insert(onConflict = REPLACE)
    fun insertTvFavorite(tvData: TvData)

    @Insert(onConflict = REPLACE)
    fun insertMovieFavoriteCursor(movieData: MovieData): Long

    @Insert(onConflict = REPLACE)
    fun insertTvFavoriteCursor(tvData: TvData): Long

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

    @Insert(onConflict = REPLACE)
    fun insertWidget(filwmWidgetData: FilmWidgetData)

    @Query("delete from widget where id = :id")
    fun deleteWidget(id: Int)

    @Query("delete from movie where id = :id")
    fun deleteMovieFavoriteById(id: Int)

    @Query("delete from tv where id = :id")
    fun deleteTvFavoriteById(id: Int)

    @Query("select * from widget")
    fun widgetData(): List<FilmWidgetData>

    @Query("select * from widget")
    fun widgetDataLive(): LiveData<List<FilmWidgetData>>
}