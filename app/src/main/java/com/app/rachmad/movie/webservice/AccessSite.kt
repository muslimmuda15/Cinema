package com.app.rachmad.movie.webservice

import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.`object`.MovieBaseData
import com.app.rachmad.movie.`object`.MovieDetailData
import com.app.rachmad.movie.`object`.TvBaseData
import com.app.rachmad.movie.`object`.TvDetailData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/3/movie/{movie_id}?api_key=${BuildConfig.Key}")
    fun movieDetail(
            @Path("movie_id") movieId: Int,
            @Query("language") language: String
    ): Call<MovieDetailData>

    @GET("/3/tv/{tv_id}?api_key=${BuildConfig.Key}")
    fun tvDetail(
            @Path("tv_id") movieId: Int,
            @Query("language") language: String
    ): Call<TvDetailData>
}