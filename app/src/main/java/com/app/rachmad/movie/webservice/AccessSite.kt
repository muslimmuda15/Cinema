package com.app.rachmad.movie.webservice

import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.`object`.GenreBaseData
import com.app.rachmad.movie.`object`.GenreData
import com.app.rachmad.movie.`object`.MovieBaseData
import com.app.rachmad.movie.`object`.TvBaseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AccessSite{

    @GET("/3/discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&api_key=${BuildConfig.Key}")
    fun movieSite(
            @Query("page") page: Int,
            @Query("language") language: String
    ): Call<MovieBaseData>

    @GET("/3/discover/tv?sort_by=popularity.desc&include_adult=false&include_video=false&api_key=${BuildConfig.Key}")
    fun tvSite(
            @Query("page") page: Int,
            @Query("language") language: String
    ): Call<TvBaseData>
}