package com.app.rachmad.movie.ui.tv

import android.appwidget.AppWidgetManager
import android.content.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.FilmWidgetData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.`object`.TvDetailData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.BaseActivity
import com.app.rachmad.movie.ui.FavoriteActivity
import com.app.rachmad.movie.ui.helper.UnfavoriteDialog
import com.app.rachmad.movie.ui.reminder.ReminderActivity
import com.app.rachmad.movie.widget.FavoriteWidget
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_tv_details.*
import kotlinx.android.synthetic.main.custom_cast.view.*
import kotlinx.android.synthetic.main.custom_chip.view.*
import java.text.SimpleDateFormat
import java.util.*

const val TV_EXTRA = "TvExtra"
class TvDetailsActivity : BaseActivity() {
    private lateinit var tvData: TvData
    var imageHeight = 0

    private fun loadData(tvDetailData: TvDetailData?){
        tvDetailData?.let {
            with(tvDetailData) {
                supportActionBar?.title = HtmlCompat.fromHtml("<font color='#ffffff'>${tvData.name}</font>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                GlideApp.with(image)
                        .load(BuildConfig.IMAGE_URL + backdrop_path)
                        .centerCrop()
                        .listener(object: RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                back_drop_image_layout.stopShimmer()
                                back_drop_image_layout.setShimmer(null)
                                return false
                            }
                        })
                        .into(image)

                GlideApp.with(poster_image)
                        .load(BuildConfig.IMAGE_URL + poster_path)
                        .centerCrop()
                        .listener(object: RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                poster_image_layout.stopShimmer()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                poster_image_layout.stopShimmer()
                                poster_image_layout.setShimmer(null)
                                return false
                            }
                        })
                        .into(poster_image)

                title_tv.text = name
                overview_text.text = if(overview.isBlank())
                    HtmlCompat.fromHtml("<i>${getString(R.string.no_preview)}</i>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                else
                    overview

                var df = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val newDate = df.parse(first_air_date)
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

                if(created_by.size > 0) {
                    cast_layout.visibility = ViewGroup.VISIBLE

                    created_by.forEach {
                        val creators = layoutInflater.inflate(R.layout.custom_cast, null) as FrameLayout
                        GlideApp.with(creators.image_cast)
                                .load(BuildConfig.IMAGE_URL + it.profile_path)
                                .centerCrop()
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                        creators.image_shimmer.stopShimmer()
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                        creators.image_shimmer.stopShimmer()
                                        creators.image_shimmer.setShimmer(null)
                                        return false
                                    }
                                })
                                .into(creators.image_cast)

                        creators.name_cast.text = it.name
                        cast_list.addView(creators)
                    }
                }
                else{
                    cast_layout.visibility = ViewGroup.GONE
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_details)

        setupLanguage()

        tvData = intent.getParcelableExtra(TV_EXTRA) as TvData

        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        toolbar.background.alpha = 0

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        val connection = viewModel.connectionTvDetail()
        viewModel.tvDetail(tvData.id, LanguageProvide.getLanguage(this))

        connection.observe(this, Observer<Int> {
            if(statusConnection(it)){
                loadData(viewModel.getTvDetail())
            }
        })

        setupListener()
    }

    private fun statusConnection(status: Int?): Boolean{
        status?.let {
            when(it){
                Status.LOADING -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    tv_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    scroll.visibility = ViewGroup.GONE
                    return false
                }
                Status.ACCEPTED -> {
                    loading_layout.visibility = ViewGroup.GONE
                    tv_loading.visibility = View.GONE
                    tv_error.visibility = View.GONE
                    scroll.visibility = ViewGroup.VISIBLE
                    return true
                }
                else -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    tv_loading.visibility = View.GONE
                    tv_error.visibility = View.VISIBLE
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
        val error = viewModel.errorTvDetail()
        tv_error.text = error?.status_message ?: run { "" }
    }

    private fun setupListener(){
        imageHeight = resources.getDimension(R.dimen.back_drop_height_image).toInt()

        scroll.viewTreeObserver.addOnScrollChangedListener {
            Log.d("data", "SCROLL Y : " + scroll.scrollY)
            if(scroll.scrollY == 0){
                toolbar.background.alpha = 0
                Log.d("scroll", "transparent : " + 0)
            }
            else if(scroll.scrollY > 0){
                val transparent = ((scroll.scrollY.toFloat() / imageHeight.toFloat()) * 255.toFloat()).toInt()
                Log.d("scroll", "(${scroll.scrollY.toFloat()} / ${imageHeight.toFloat()}) * ${255.toFloat()}")
                Log.d("scroll", "transparent : $transparent")
                if (transparent <= 255)
                    toolbar.background.alpha = transparent
                else if(transparent > 255)
                    toolbar.background.alpha = 255
            }
        }

        favorite_button.setOnClickListener {
            if(viewModel.isFavoritedTv(tvData.id)){
                val unFavoriteDialog = UnfavoriteDialog(this, viewModel, tvData)
                unFavoriteDialog.show()
                unFavoriteDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            else {
                viewModel.insertTvFavorite(tvData)
                val filmWidgetData = FilmWidgetData(
                        0,
                        tvData.name,
                        tvData.backdrop_path
                )
                viewModel.insertFilmWidget(filmWidgetData)
            }
        }

        val favorite = viewModel.countFavoritedTvLive(tvData.id)
        favorite.observe(this, Observer<Int> {
            favorite_button.setImageResource(if(it > 0)
                R.drawable.ic_favorite_black_24dp
            else
                R.drawable.ic_favorite_border_black_24dp)
        })
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
            R.id.favorite_list -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.reminder -> {
                val intent = Intent(this, ReminderActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
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