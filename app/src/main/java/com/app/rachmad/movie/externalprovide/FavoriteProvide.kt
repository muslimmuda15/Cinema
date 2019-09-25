package com.app.rachmad.movie.externalprovide

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.app.rachmad.movie.App
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.sqlite.model.FavoriteModel
import android.content.UriMatcher
import android.database.SQLException
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.Status
import java.lang.RuntimeException

class FavoriteProvide : ContentProvider() {
    lateinit var favoriteModel: FavoriteModel
    val uriMatcher: UriMatcher

    companion object {
        val CONTENT_URI1 = Uri.parse("content://${BuildConfig.PROVIDE_NAME}/movie")
        val CONTENT_URI2 = Uri.parse("content://${BuildConfig.PROVIDE_NAME}/tv")
    }

    init {
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(BuildConfig.PROVIDE_NAME, "movie", Status.MOVIE);
        uriMatcher.addURI(BuildConfig.PROVIDE_NAME, "movie/#", Status.MOVIE_ID);
        uriMatcher.addURI(BuildConfig.PROVIDE_NAME, "tv", Status.TV);
        uriMatcher.addURI(BuildConfig.PROVIDE_NAME, "tv/#", Status.TV_ID);
    }

    override fun onCreate(): Boolean {
        favoriteModel = FavoriteModel(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        when(uriMatcher.match(uri)){
            Status.MOVIE -> {
                return favoriteModel.getFavoriteMovieCursor()
            }
            Status.TV -> {
                return favoriteModel.getFavoriteTvCursor()
            }
            else -> {
                throw SQLException("Failed insert rom into $uri")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val _uri: Uri
        when(uriMatcher.match(uri)){
            Status.MOVIE -> {
                val value = values as MovieData
                val id = favoriteModel.insertMovieFavoriteCursor(value)
                context?.contentResolver?.notifyChange(CONTENT_URI1, null)
                _uri = ContentUris.withAppendedId(uri, id)
            }
            Status.TV -> {
                val value = values as TvData
                val id = favoriteModel.insertTvFavoriteCursor(value)
                context?.contentResolver?.notifyChange(CONTENT_URI2, null)
                _uri = ContentUris.withAppendedId(uri, id)
            }
            else -> {
                throw SQLException("Failed insert rom into $uri")
            }
        }
        return _uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(uriMatcher.match(uri)) {
            Status.MOVIE -> {
                favoriteModel.deleteMovieFavoriteByName(selection ?: "none")
                return favoriteModel.countAllFavoriteMovie()
            }
            Status.TV -> {
                favoriteModel.deleteTvFavoriteByName(selection ?: "none")
                return favoriteModel.countAllFavoriteTv()
            }
        }
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}
