package com.app.rachmad.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.rachmad.movie.App
import com.app.rachmad.movie.`object`.*
import com.app.rachmad.movie.repository.MovieRepository
import com.app.rachmad.movie.repository.TvRepository
import com.app.rachmad.movie.sqlite.model.FavoriteModel

class ListModel: ViewModel(){
    val movieRepository: MovieRepository = MovieRepository()
    val tvRepository: TvRepository = TvRepository()
    val favoriteModel: FavoriteModel = FavoriteModel(App.context)

    val movieFavoriteLiveData: LiveData<PagedList<MovieData>>
    val tvFavoriteLiveData: LiveData<PagedList<TvData>>

    init {
        val movieFactory: DataSource.Factory<Int, MovieData>? = favoriteModel.getFavoriteMovieList()
        val moviePagedBuilder: LivePagedListBuilder<Int, MovieData> = LivePagedListBuilder(movieFactory!!, 20)
        movieFavoriteLiveData = moviePagedBuilder.build()

        val tvFactory: DataSource.Factory<Int, TvData>? = favoriteModel.getFavoriteTvList()
        val tvPagedBuilder: LivePagedListBuilder<Int, TvData> = LivePagedListBuilder(tvFactory!!, 20)
        tvFavoriteLiveData = tvPagedBuilder.build()
    }

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

    fun insertMovieFavorite(movieData: MovieData) = favoriteModel.insertMovieFavorite(movieData)
    fun insertTvFavorite(tvData: TvData) = favoriteModel.insertTvFavorite(tvData)
    fun countAllFavoriteMovie(): Int = favoriteModel.countAllFavoriteMovie()
    fun countAllFavoriteTv(): Int = favoriteModel.countAllFavoriteTv()
    fun countFavoritedMovieLive(id: Int): LiveData<Int> = favoriteModel.countFavoritedMovieLive(id)
    fun countFavoritedTvLive(id: Int): LiveData<Int> = favoriteModel.countFavoritedTvLive(id)
    fun deleteMovieFavorite(movieData: MovieData) = favoriteModel.deleteMovieFavorite(movieData)
    fun deleteTvFavorite(tvData: TvData) = favoriteModel.deleteTvFavorite(tvData)
    fun isFavoritedMovie(id: Int): Boolean = favoriteModel.isFavoritedMovie(id)
    fun isFavoritedTv(id: Int): Boolean = favoriteModel.isFavoritedTv(id)
}