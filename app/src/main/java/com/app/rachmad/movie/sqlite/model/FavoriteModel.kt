package com.app.rachmad.movie.sqlite.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.sqlite.CinemaDatabase

class FavoriteModel(c: Context) {
    val database = CinemaDatabase.getInstance(c)

    fun getFavoriteMovieList(): DataSource.Factory<Int, MovieData>?{
        var favoriteList: DataSource.Factory<Int, MovieData>? = null
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteMovieList()
        }
        return favoriteList
    }

    fun getFavoriteTvList(): DataSource.Factory<Int, TvData>?{
        var favoriteList: DataSource.Factory<Int, TvData>? = null
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteTvList()
        }
        return favoriteList
    }

    fun getFavoriteMovieStackList(): LiveData<List<MovieData>>{
        var favoriteList: LiveData<List<MovieData>> = MutableLiveData()
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteMovieStackList()
        }
        return favoriteList
    }

    fun getFavoriteMovie(): List<MovieData>{
        var favoriteList: List<MovieData> = ArrayList()
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteMovie()
        }
        return favoriteList
    }

    fun getFavoriteTvStackList(): LiveData<List<TvData>>{
        var favoriteList: LiveData<List<TvData>> = MutableLiveData()
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteTvStackList()
        }
        return favoriteList
    }

    fun countAllFavoriteMovie(): Int {
        return database?.favoriteDatabase()?.countAllFavoriteMovie() ?: 0
    }

    fun countAllFavoriteTv(): Int {
        return database?.favoriteDatabase()?.countAllFavoriteTv() ?: 0
    }

    fun insertMovieFavorite(movieData: MovieData){
        database?.favoriteDatabase()?.insertMovieFavorite(movieData)
    }

    fun insertTvFavorite(tvData: TvData){
        database?.favoriteDatabase()?.insertTvFavorite(tvData)
    }

    fun countFavoritedMovieLive(id: Int): LiveData<Int>{
        var favorited: LiveData<Int> = MutableLiveData()
        database?.let {
            favorited = it.favoriteDatabase().countFavoriteMovieLive(id)
        }
        return favorited
    }

    fun countFavoritedTvLive(id: Int): LiveData<Int>{
        var favorited: LiveData<Int> = MutableLiveData()
        database?.let {
            favorited = it.favoriteDatabase().countFavoriteTvLive(id)
        }
        return favorited
    }

    fun deleteMovieFavorite(movieData: MovieData){
        database?.favoriteDatabase()?.deleteMovieFavorite(movieData)
    }

    fun deleteTvFavorite(tvData: TvData){
        database?.favoriteDatabase()?.deleteTvFavorite(tvData)
    }

    fun isFavoritedMovie(id: Int): Boolean{
        var favorited = false
        database?.let {
            favorited = if(it.favoriteDatabase().countFavoriteMovie(id) > 0)
                true
            else
                false
        }
        return favorited
    }

    fun isFavoritedTv(id: Int): Boolean{
        var favorited = false
        database?.let {
            favorited = if(it.favoriteDatabase().countFavoriteTv(id) > 0)
                true
            else
                false
        }
        return favorited
    }

    fun insertWidgetFavorite(filmWidgetData: FilmWidgetData){
        database?.favoriteDatabase()?.insertWidget(filmWidgetData)
    }

    fun getWidgetFavorite(): List<FilmWidgetData>?{
        return database?.favoriteDatabase()?.widgetData()
    }

    fun deleteWidgetFavorite(name: String){
        database?.favoriteDatabase()?.deleteWidget(name)
    }

    fun getWIdgetFavoriteLive(): LiveData<List<FilmWidgetData>>{
        var data: LiveData<List<FilmWidgetData>> = MutableLiveData()
        database?.let {
            data = it.favoriteDatabase().widgetDataLive()
        }
        return data
    }
}