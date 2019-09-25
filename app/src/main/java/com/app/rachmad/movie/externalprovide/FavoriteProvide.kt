package com.app.rachmad.movie.externalprovide

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.sqlite.model.FavoriteModel
import android.content.UriMatcher
import android.database.SQLException
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.Status

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
            Status.MOVIE_ID -> {
                return favoriteModel.getSingleFavoriteMovieCursor(uri.lastPathSegment!!.toInt())
            }
            Status.TV_ID -> {
                return favoriteModel.getSingleFavoriteTvCursor(uri.lastPathSegment!!.toInt())
            }
            else -> {
                throw SQLException("Failed insert rom into $uri")
            }
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        var _uri: Uri? = null
        when(uriMatcher.match(uri)){
            Status.MOVIE_ID -> {
                var id = 0L
                values?.apply {
                    val data = MovieData(
                            getAsInteger("id"),
                            getAsString("poster_path"),
                            getAsBoolean("adult"),
                            getAsString("overview"),
                            getAsString("release_date"),
                            getAsString("original_title"),
                            getAsString("original_language"),
                            getAsString("title"),
                            getAsString("backdrop_path"),
                            getAsFloat("popularity"),
                            getAsInteger("vote_count"),
                            getAsBoolean("video"),
                            getAsFloat("vote_average"))
                    id = favoriteModel.insertMovieFavoriteCursor(data)


                    val filmWidgetData = FilmWidgetData(
                            0,
                            data.title,
                            data.backdrop_path
                    )
                    favoriteModel.insertWidgetFavorite(filmWidgetData)

                    context?.contentResolver?.notifyChange(CONTENT_URI1, null)
                    _uri = ContentUris.withAppendedId(uri, id)
                }
            }
            Status.TV_ID -> {
                var id = 0L
                values?.apply {
                    val data = TvData(
                            getAsInteger("id"),
                            getAsString("poster_path"),
                            getAsFloat("popularity"),
                            getAsString("backdrop_path"),
                            getAsFloat("vote_average"),
                            getAsString("overview"),
                            getAsString("first_air_date"),
                            getAsString("original_language"),
                            getAsInteger("vote_count"),
                            getAsString("name"),
                            getAsString("original_name"))
                    id = favoriteModel.insertTvFavoriteCursor(data)


                    val filmWidgetData = FilmWidgetData(
                            0,
                            data.name,
                            data.backdrop_path
                    )
                    favoriteModel.insertWidgetFavorite(filmWidgetData)

                    context?.contentResolver?.notifyChange(CONTENT_URI2, null)
                    _uri = ContentUris.withAppendedId(uri, id)
                }
            }
            else -> {
                throw SQLException("Failed insert rom into $uri")
            }
        }
        return _uri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(uriMatcher.match(uri)) {
            Status.MOVIE_ID -> {
                favoriteModel.deleteMovieFavoriteById(uri.lastPathSegment?.toInt() ?: 0)
                favoriteModel.deleteWidgetFavorite(uri.lastPathSegment?.toInt() ?: 0)
                return favoriteModel.countAllFavoriteMovie()
            }
            Status.TV_ID -> {
                favoriteModel.deleteTvFavoriteById(uri.lastPathSegment?.toInt() ?: 0)
                favoriteModel.deleteWidgetFavorite(uri.lastPathSegment?.toInt() ?: 0)
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
