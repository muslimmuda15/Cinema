package com.app.rachmad.movie.ui.movie.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.ui.BaseActivity
import com.app.rachmad.movie.ui.movie.MOVIE_EXTRA
import com.app.rachmad.movie.ui.movie.MovieDetailsActivity

class MovieSearchActivity : BaseActivity(), MovieSearchItemFragment.OnMovieSearchListener {
    override fun onMovieSearch(item: MovieData) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_EXTRA, item)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        supportFragmentManager.beginTransaction().add(R.id.container, MovieSearchItemFragment()).commit()
    }
}
