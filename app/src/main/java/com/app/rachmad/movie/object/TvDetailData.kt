package com.app.rachmad.movie.`object`

data class TvDetailData (
        val backdrop_path: String?,
        val created_by: List<CreatedBy>,
        val episode_run_time: List<Int>,
        val first_air_date: String,
        val genres: List<GenreData>,
        val homepage: String?,
        val id: Int,
        val in_production: Boolean,
        val languages: List<String>,
        val last_air_date: String,
        val last_episode_to_air: LastEpisodeToAir,
        val name: String,
        val networks: List<Network>,
        val number_of_episodes: Int,
        val number_of_seasons: Int,
        val origin_country: List<String>,
        val original_language: String,
        val original_name: String,
        val overview: String,
        val popularity: Float,
        val poster_path: String?,
        val production_companies: List<Companies>,
        val seasons: List<Seasons>,
        val status: String,
        val type: String,
        val vote_average: Float,
        val vote_count: Int
)

data class CreatedBy(
        val id: Int,
        val credit_id: String,
        val name: String,
        val gender: Int,
        val profile_path: String
)

data class LastEpisodeToAir(
        val air_date: String,
        val episode_number: Int,
        val id: Int,
        val name: String,
        val overview: String,
        val production_code: String,
        val season_number: Int,
        val show_id: Int,
        val still_path: String,
        val vote_average: Float,
        val vote_count: Int
)

data class Network(
        val name: String,
        val id: Int,
        val logo_path: String,
        val origin_country: String
)

data class Seasons(
        val air_date: String,
        val episode_count: Int,
        val id: Int,
        val name: String,
        val overview: String,
        val poster_path: String,
        val season_number: String
)