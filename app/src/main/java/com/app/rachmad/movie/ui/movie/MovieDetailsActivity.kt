package com.app.rachmad.movie.ui.movie

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.MovieDetailData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.tv.TV_EXTRA
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.custom_chip.view.*
import java.text.SimpleDateFormat
import java.util.*

const val MOVIE_EXTRA = "MovieExtra"
class MovieDetailsActivity : AppCompatActivity() {
    lateinit var movieData: MovieData
    lateinit var viewModel: ListModel
    var imageHeight = 0

    private val langReceiver by lazy {
        object: BroadcastReceiver(){
            override fun onReceive(c: Context, i: Intent) {
                Log.d("language", "LANGUAGE CHANGED TO " + Locale.getDefault().getCountry())
                val data = intent.getParcelableExtra(TV_EXTRA) as MovieData

                viewModel?.refreshMovieDetail()
                viewModel?.movieDetail(data.id, LanguageProvide.getLanguage(c))
            }
        }
    }


    private fun loadData(movieDetailData: MovieDetailData?){
        movieDetailData?.let {
            with(movieDetailData) {
                supportActionBar?.title = HtmlCompat.fromHtml("<font color='#ffffff'>${title}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                GlideApp.with(image)
                        .load(BuildConfig.IMAGE_URL + backdrop_path)
                        .centerCrop()
                        .into(image)

                GlideApp.with(poster_image)
                        .load(BuildConfig.IMAGE_URL + poster_path)
                        .centerCrop()
                        .into(poster_image)

                title_movie.text = title
                overview_text.text = if(overview.isNullOrBlank())
                    HtmlCompat.fromHtml("<i>${getString(R.string.no_preview)}</i>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                else
                    overview

                var df = SimpleDateFormat("yyyy-mm-dd", Locale.US)
                val newDate = df.parse(release_date)
                df = SimpleDateFormat("MMM yyyy", Locale.US)
                date_release.text = df.format(newDate)

                rating_star.rating = vote_average / 2
                rating_text.text = vote_average.toString()

                votes.text = resources.getQuantityString(R.plurals.votes, vote_count, vote_count)

                genres.forEach { genre_id ->
                    val genreCard = layoutInflater.inflate(R.layout.custom_chip, null) as FrameLayout
                    genreCard.genre_text.text = genre_id.name
                    genres_layout.addView(genreCard)

                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        viewModel = ViewModelProviders.of(this).get(ListModel::class.java)
        setupLanguage()

        movieData = intent.getParcelableExtra(MOVIE_EXTRA) as MovieData

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val connection = viewModel.connectionMovieDetail()
        viewModel.movieDetail(movieData.id, LanguageProvide.getLanguage(this))

        connection.observe(this, Observer<Int> {
            if(statusConnection(it)){
                loadData(viewModel.getMovieDetail())
            }
        })

        setupListener()
    }

    private fun statusConnection(status: Int?): Boolean{
        status?.let {
            when(it){
                Status.LOADING -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    movie_loading.visibility = View.VISIBLE
                    movie_error.visibility = View.GONE
                    scroll.visibility = ViewGroup.GONE
                    return false
                }
                Status.ACCEPTED -> {
                    loading_layout.visibility = ViewGroup.GONE
                    movie_loading.visibility = View.GONE
                    movie_error.visibility = View.GONE
                    scroll.visibility = ViewGroup.VISIBLE
                    return true
                }
                else -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    movie_loading.visibility = View.GONE
                    movie_error.visibility = View.VISIBLE
                    scroll.visibility = ViewGroup.GONE
                    sendError()
                    return false
                }
            }
        } ?: run {
            sendError()
            return false
        }
    }

    private fun sendError(){
        val error = viewModel.errorMovieDetail()
        movie_error.text = error?.status_message ?: run { "" }
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

    protected fun setupLanguage(){
        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(langReceiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(langReceiver)
        super.onDestroy()
    }
}
