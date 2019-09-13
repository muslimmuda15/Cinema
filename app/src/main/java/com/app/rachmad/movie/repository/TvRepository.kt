package com.app.rachmad.movie.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.app.rachmad.movie.`object`.ErrorData
import com.app.rachmad.movie.`object`.TvBaseData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.`object`.TvDetailData
import com.app.rachmad.movie.datasource.TvDataSource
import com.app.rachmad.movie.helper.MainThreadExecutor
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.webservice.MovieSite
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvRepository {
    var connectionTvList = MutableLiveData<Int>()
    var connectionTvDetail = MutableLiveData<Int>()
    val tvDataSource = TvDataSource()
    var errorTvList: ErrorData? = null
    var errorTvDetails: ErrorData? = null
    var tvList: PagedList<TvData>? = null
    var tvDetailsData: TvDetailData? = null

    fun refreshTvDetail(){
        connectionTvDetail.value = null
        tvDetailsData = null
    }

    fun tvDetails(tvId: Int, language: String){
        if(connectionTvDetail.value == null && tvDetailsData == null){
            var service = MovieSite.connect()
            val call = service.tvDetail(tvId, language)

            connectionTvDetail.value = Status.LOADING
            call.enqueue(object: Callback<TvDetailData>{
                override fun onFailure(call: Call<TvDetailData>, t: Throwable) {
                    errorTvDetails = ErrorData(Status.UNKNOWN, t.message)
                    connectionTvDetail.value = -1
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<TvDetailData>, response: Response<TvDetailData>) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            tvDetailsData = it
                            connectionTvDetail.value = Status.ACCEPTED
                        } ?: run {
                            connectionTvList.value = -1
                        }
                    }
                    else{
                        val errorResponse = response.errorBody().toString()
//                        val type = object: TypeToken<ErrorData>() {}.type
//                        val error = Gson().fromJson<ErrorData>(response.errorBody()?.charStream(), type)
                        val error = Gson().fromJson(errorResponse, ErrorData::class.java)
                        errorTvDetails = ErrorData(error.status_code, error.status_message)
                        connectionTvDetail.value = error.status_code
                    }
                }
            })
        }
    }

    fun refreshTv(){
        connectionTvList.value = null
        tvList = null
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