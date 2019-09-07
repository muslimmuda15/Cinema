package com.app.rachmad.movie.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.app.rachmad.movie.`object`.*
import com.app.rachmad.movie.datasource.MovieDataSource
import com.app.rachmad.movie.datasource.TvDataSource
import com.app.rachmad.movie.helper.MainThreadExecutor
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.webservice.MovieSite
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository{
    var connectionMovieList = MutableLiveData<Int>()
    var connectionTvList = MutableLiveData<Int>()
    var connectionGenreMovie = MutableLiveData<Int>()
    var connectionGenreTv = MutableLiveData<Int>()

    val movieDataSource = MovieDataSource()
    val tvDataSource = TvDataSource()

    var errorMovieList: ErrorData? = null
    var errorTvList: ErrorData? = null

    var movieList: PagedList<MovieData>? = null
    var tvList: PagedList<TvData>? = null

    fun refreshMovie(){
        connectionMovieList.value = null
        connectionGenreMovie.value = null
        movieList = null
    }

    fun refreshTv(){
        connectionGenreTv.value = null
        connectionTvList.value = null
        tvList = null
    }

    fun movie(language: String){
        if(connectionMovieList.value == null) {
            val service = MovieSite.connect()
            val call = service.movieSite(1, language)

            connectionMovieList.value = Status.LOADING
            call.enqueue(object : Callback<MovieBaseData> {
                override fun onFailure(call: Call<MovieBaseData>, t: Throwable) {
                    errorMovieList = ErrorData(Status.UNKNOWN, t.message)
                    connectionMovieList.value = -1
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<MovieBaseData>, response: Response<MovieBaseData>) {
                    if(response.isSuccessful) {
                        response.body()?.let {
                            if (it.total_pages > 0) {
                                movieDataSource.setData(it.results, language)
                                val exec = MainThreadExecutor()
                                val config: PagedList.Config = PagedList.Config.Builder()
                                        .setPageSize(it.total_results)
                                        .setInitialLoadSizeHint(20)
                                        .setEnablePlaceholders(false)
                                        .setPrefetchDistance(2)
                                        .build()

                                val moviePagedList = PagedList.Builder(movieDataSource, config)
                                        .setFetchExecutor(exec)
                                        .setNotifyExecutor(exec)
                                        .build()

                                movieList = moviePagedList
                                connectionMovieList.value = Status.ACCEPTED

                            } else {
                                errorMovieList = ErrorData(Status.EMPTY_DATA, "")
                                connectionMovieList.value = -1
                            }
                        } ?: run {
                            //                        errorMovieList = ErrorData(response.errorBody(), Status.EMPTY_DATA)
                            connectionMovieList.value = -1
                        }
                    }
                    else{
                        Log.d("OkHttp", "MOVIE NOT SUCCESS : ")
                        val type = object: TypeToken<ErrorData>() {}.type
                        val error = Gson().fromJson<ErrorData>(response.errorBody()?.charStream(), type)

                        errorMovieList = ErrorData(error.status_code, error.status_message)
                        connectionMovieList.value = error.status_code
                    }
                }
            })
        }
    }

    fun tv(language: String){
        if(connectionTvList.value == null) {
            val service = MovieSite.connect()
            val call = service.tvSite(1, language)

            connectionTvList.value = Status.LOADING
            call.enqueue(object : Callback<TvBaseData> {
                override fun onFailure(call: Call<TvBaseData>, t: Throwable) {
                    errorTvList = ErrorData(Status.UNKNOWN, t.message)
                    connectionTvList.value = -1
                    Log.d("OkHttp", "TV FAILURE")
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<TvBaseData>, response: Response<TvBaseData>) {
                    Log.d("OkHttp", "TV SUCCESS")
                    if(response.isSuccessful) {
                        Log.d("OkHttp", "TV IS SUCCESS")
                        response.body()?.let {
                            if (it.total_pages > 0) {
                                tvDataSource.setData(it.results, language)
                                val exec = MainThreadExecutor()
                                val config: PagedList.Config = PagedList.Config.Builder()
                                        .setPageSize(it.total_results)
                                        .setInitialLoadSizeHint(20)
                                        .setEnablePlaceholders(false)
                                        .setPrefetchDistance(2)
                                        .build()

                                val tvPagedList = PagedList.Builder(tvDataSource, config)
                                        .setFetchExecutor(exec)
                                        .setNotifyExecutor(exec).build()

                                tvList = tvPagedList
                                connectionTvList.value = Status.ACCEPTED

                            } else {
                                errorTvList = ErrorData(Status.EMPTY_DATA, "")
                                connectionTvList.value = -1
                            }
                        } ?: run {
                            errorTvList = ErrorData(Status.UNKNOWN, "")
                            connectionTvList.value = -1
                        }
                    }
                    else{
                        Log.d("OkHttp", "TV NOT SUCCESS : ")
                        val type = object: TypeToken<ErrorData>() {}.type
                        val error = Gson().fromJson<ErrorData>(response.errorBody()?.charStream(), type)

                        errorTvList = ErrorData(error.status_code, error.status_message)
                        connectionTvList.value = error.status_code
                    }
                }
            })
        }
    }
}