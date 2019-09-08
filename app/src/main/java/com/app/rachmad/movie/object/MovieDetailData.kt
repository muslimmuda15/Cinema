package com.app.rachmad.movie.`object`

data class MovieDetailData (
        val adult: Boolean?,
        val backdrop_path: String?,
        val budget: Int,
        val genres: List<GenreData>,
        val homepage: String?,
        val id: Int,
        val imdb_id: String?,
        val original_language: String,
        val original_title: String,
        val overview: String?,
        val popularity: Float,
        val poster_path: String?,
        val production_companies: List<Companies>,
        val production_countries: List<Countries>,
        val release_date: String,
        val revenue: Int,
        val runtime: Int?,
        val spoken_languages: List<Speakes>,
        val status: String,
        val tagline: String?,
        val title: String,
        val video: Boolean,
        val vote_average: Float,
        val vote_count: Int
)

data class GenreData(
        val id: Int,
        val name: String
)

data class Companies(
        val name: String,
        val id: Int,
        val logo_path: String?,
        val origin_country: String
)

data class Countries(
        val iso_3166_1: String,
        val name: String
)

data class Speakes(
        val iso_639_1: String,
        val name: String
)