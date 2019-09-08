package com.app.rachmad.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.app.rachmad.movie.`object`.*
import com.app.rachmad.movie.repository.MovieRepository
import com.app.rachmad.movie.repository.TvRepository

class ListModel: ViewModel(){
    val movieRepository: MovieRepository = MovieRepository()
    val tvRepository: TvRepository = TvRepository()

    fun movie(language: String) = movieRepository.movie(language)
    fun connectionMovie(): LiveData<Int> = movieRepository.connectionMovieList
    fun getMovieList(): PagedList<MovieData>? = movieRepository.movieList
    fun errorMovie(): ErrorData? = movieRepository.errorMovieList

    fun tv(language: String) = tvRepository.tv(language)
    fun connectionTv(): LiveData<Int> = tvRepository.connectionTvList
    fun getTvList(): PagedList<TvData>? = tvRepository.tvList
    fun errorTv(): ErrorData? = tvRepository.errorTvList

    fun refreshMovie() = movieRepository.refreshMovie()
    fun refreshTv() = tvRepository.refreshTv()
    fun refreshMovieDetail() = movieRepository.refreshMovieDetail()
    fun refreshTvDetail() = tvRepository.refreshTvDetail()

    fun doLoadingMovie(): LiveData<Boolean> =  movieRepository.movieDataSource.doLoading
    fun doLoadingTv(): LiveData<Boolean> = tvRepository.tvDataSource.doLoading

    fun movieDetail(movieId: Int, language: String) = movieRepository.movieDetails(movieId, language)
    fun connectionMovieDetail(): LiveData<Int> = movieRepository.connectionMovieDetail
    fun errorMovieDetail(): ErrorData? = movieRepository.errorMovieDetails
    fun getMovieDetail(): MovieDetailData? = movieRepository.movieDetailsData

    fun tvDetail(movieId: Int, language: String) = tvRepository.tvDetails(movieId, language)
    fun connectionTvDetail(): LiveData<Int> = tvRepository.connectionTvDetail
    fun errorTvDetail(): ErrorData? = tvRepository.errorTvDetails
    fun getTvDetail(): TvDetailData? = tvRepository.tvDetailsData
}