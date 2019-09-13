package com.app.rachmad.movie.sqlite.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.rachmad.movie.sqlite.CinemaDatabase
import com.app.rachmad.movie.sqlite.table.FavoriteTable

class FavoriteModel(val c: Context) {
    val database = CinemaDatabase.getInstance(c)

    fun getFavoriteMovieList(): List<FavoriteTable>{
        var favoriteList: List<FavoriteTable> = ArrayList()
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteMovieList()
        }
        return favoriteList
    }

    fun getFavoriteTvList(): List<FavoriteTable>{
        var favoriteList: List<FavoriteTable> = ArrayList()
        database?.let {
            favoriteList = it.favoriteDatabase().getFavoriteTvList()
        }
        return favoriteList
    }

    fun insertFavorite(favoriteTable: FavoriteTable){
        database?.favoriteDatabase()?.insertFavorite(favoriteTable)
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

    fun deleteFavorite(favoriteTable: FavoriteTable){
        database?.favoriteDatabase()?.deleteFavorite(favoriteTable)
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
}