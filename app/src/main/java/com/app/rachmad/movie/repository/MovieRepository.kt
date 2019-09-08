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
    var connectionMovieDetail = MutableLiveData<Int>()
    val movieDataSource = MovieDataSource()
    var errorMovieList: ErrorData? = null
    var errorMovieDetails: ErrorData? = null
    var movieList: PagedList<MovieData>? = null
    var movieDetailsData: MovieDetailData? = null

    fun refreshMovie(){
        connectionMovieList.value = null
        movieList = null
    }

    fun refreshMovieDetail(){
        connectionMovieDetail.value = null
        movieDetailsData = null
    }

    fun movieDetails(movieId: Int, language: String){
        if(connectionMovieDetail.value == null && movieDetailsData == null){
            var service = MovieSite.connect()
            val call = service.movieDetail(movieId, language)

            connectionMovieDetail.value = Status.LOADING
            call.enqueue(object: Callback<MovieDetailData>{
                override fun onFailure(call: Call<MovieDetailData>, t: Throwable) {
                    errorMovieDetails = ErrorData(Status.UNKNOWN, t.message)
                    connectionMovieDetail.value = -1
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<MovieDetailData>, response: Response<MovieDetailData>) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            movieDetailsData = it
                            connectionMovieDetail.value = Status.ACCEPTED
                        } ?: run {
                            connectionMovieList.value = -1
                        }
                    }
                    else{
                        val type = object: TypeToken<ErrorData>() {}.type
                        val error = Gson().fromJson<ErrorData>(response.errorBody()?.charStream(), type)

                        errorMovieDetails = ErrorData(error.status_code, error.status_message)
                        connectionMovieDetail.value = error.status_code
                    }
                }
            })
        }
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
}