package com.app.rachmad.movie.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.app.rachmad.movie.`object`.TvBaseData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.webservice.MovieSite
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvDataSource: PageKeyedDataSource<Int, TvData> {
    var resultFirst: List<TvData>? = null
    var language: String? = null
    val doLoading = MutableLiveData<Boolean>()

    constructor(): super()

    fun setData(resultFirst: List<TvData>, language: String){
        this.resultFirst = resultFirst
        this.language = language
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, TvData>) {
        val page = 1

        val result = resultFirst
        callback.onResult(result!!,
                0,
                result.size,
                null,
                page + 1)
        doLoading.setValue(false)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TvData>) {
        val page = params.key
        doLoading.setValue(true)

        val service = MovieSite.connect()
        val call = service.tvSite(page, language!!)

        call.enqueue(object : Callback<TvBaseData> {
            override fun onFailure(call: Call<TvBaseData>, t: Throwable) {
                doLoading.setValue(false)
            }

            override fun onResponse(call: Call<TvBaseData>, response: Response<TvBaseData>) {
                response.body()?.let {
                    val result = it.results
                    callback.onResult(result,
                            page + 1)
                    doLoading.setValue(false)
                }
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TvData>) {

    }
}