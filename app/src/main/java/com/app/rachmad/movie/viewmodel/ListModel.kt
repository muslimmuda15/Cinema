package com.app.rachmad.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.app.rachmad.movie.`object`.ErrorData
import com.app.rachmad.movie.`object`.GenreData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.datasource.MovieDataSource
import com.app.rachmad.movie.repository.MovieRepository

class ListModel: ViewModel(){
    val movieRepository: MovieRepository = MovieRepository()

    fun movie(language: String) = movieRepository.movie(language)
    fun connectionMovie(): LiveData<Int> = movieRepository.connectionMovieList
    fun getMovieList(): PagedList<MovieData>? = movieRepository.movieList
    fun errorMovie(): ErrorData? = movieRepository.errorMovieList

    fun tv(language: String) = movieRepository.tv(language)
    fun connectionTv(): LiveData<Int> = movieRepository.connectionTvList
    fun getTvList(): PagedList<TvData>? = movieRepository.tvList
    fun errorTv(): ErrorData? = movieRepository.errorTvList

    fun refreshMovie() = movieRepository.refreshMovie()
    fun refreshTv() = movieRepository.refreshTv()

    fun doLoadingMovie(): LiveData<Boolean> =  movieRepository.movieDataSource.doLoading
    fun doLoadingTv(): LiveData<Boolean> = movieRepository.tvDataSource.doLoading
}