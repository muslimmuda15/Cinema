package com.app.rachmad.movie.details

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProviders
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.GenreData
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.custom_chip.view.*
import java.text.SimpleDateFormat
import java.util.*

const val MOVIE_EXTRA = "MovieExtra"
const val TV_EXTRA = "TvExtra"
class MovieDetailsActivity : AppCompatActivity() {
    lateinit var movieData: MovieData
    var imageHeight = 0

    private fun loadData(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val viewModel = ViewModelProviders.of(this).get(ListModel::class.java)

        movieData = intent.getParcelableExtra<MovieData>(MOVIE_EXTRA) as MovieData

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        with(movieData) {
            supportActionBar?.title = HtmlCompat.fromHtml("<font color='#ffffff'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
            GlideApp.with(image)
                    .load("http://image.tmdb.org/t/p/w500" + backdrop_path)
                    .fitCenter()
                    .into(image)

            GlideApp.with(poster_image)
                    .load("http://image.tmdb.org/t/p/w500" + poster_path)
                    .fitCenter()
                    .into(poster_image)

            title_movie.text = title
            overview_text.text = overview

            var df = SimpleDateFormat("yyyy-mm-dd", Locale.US)
            val newDate = df.parse(release_date)
            df = SimpleDateFormat("MMM yyyy", Locale.US)
            date_release.text = df.format(newDate)

            rating_star.rating = vote_average / 2
            rating_text.text = vote_average.toString()

            votes.text = vote_count.toString() + " votes"

//            genre_ids.forEach { genre_id ->
//                val genreCard = layoutInflater.inflate(R.layout.custom_chip, null) as FrameLayout
//                val genreText = genre.find { it.id == genre_id.toInt() }
//                genreText?.let {

//                    genreCard.genre_text.text = it.name
//                    genres.addView(genreCard)
//                }
//            }
        }

        setupListener()
    }

    private fun setupListener(){
        image.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                imageHeight = image.measuredHeight
            }
        })

        scroll.viewTreeObserver.addOnScrollChangedListener {
            Log.d("data", "SCROLL Y : " + scroll.scrollY)
            if(scroll.scrollY == 0){
                toolbar.background.alpha = 0
                Log.d("scroll", "transparent : " + 0)
            }
            else if(scroll.scrollY > 0){
                val transparent = ((scroll.scrollY.toFloat() / imageHeight.toFloat()) * 255.toFloat()).toInt()
                Log.d("scroll", "(${scroll.scrollY.toFloat()} / ${imageHeight.toFloat()}) * ${255.toFloat()}")
                Log.d("scroll", "transparent : " + transparent)
                if (transparent <= 255) {
                    toolbar.background.alpha = transparent
                }
                else if(transparent > 255)
                    toolbar.background.alpha = 255
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.change_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
