package com.app.rachmad.movie.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.app.rachmad.movie.`object`.MovieBaseData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.webservice.MovieSite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDataSource: PageKeyedDataSource<Int, MovieData> {
    var resultFirst: List<MovieData>? = null
    var language: String? = null
    val doLoading = MutableLiveData<Boolean>()

    constructor(): super()

    fun setData(resultFirst: List<MovieData>, language: String){
        this.resultFirst = resultFirst
        this.language = language
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieData>) {
        val page = 1

        val result = resultFirst
        callback.onResult(result!!,
                0,
                result.size,
                null,
                page + 1)
        doLoading.setValue(false)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {
        val page = params.key
        doLoading.setValue(true)

        val service = MovieSite.connect()
        val call = service.movieSite(page, language!!)

        call.enqueue(object: Callback<MovieBaseData>{
            override fun onFailure(call: Call<MovieBaseData>, t: Throwable) {
                doLoading.setValue(false)
            }

            override fun onResponse(call: Call<MovieBaseData>, response: Response<MovieBaseData>) {
                response.body()?.let {
                    val result = it.results
                    callback.onResult(result,
                            page + 1)

                    doLoading.setValue(false)
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieData>) {

    }
}