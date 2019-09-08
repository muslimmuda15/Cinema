package com.app.rachmad.movie.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.ui.details.MOVIE_EXTRA
import com.app.rachmad.movie.ui.details.MovieDetailsActivity
import com.app.rachmad.movie.ui.details.TV_EXTRA
import com.app.rachmad.movie.ui.details.TvDetailsActivity
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.ui.movie.MovieItemFragment
import com.app.rachmad.movie.movie.TvItemFragment
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), MovieItemFragment.OnMovieClickListener, TvItemFragment.OnTvClickListener {
    var viewModel: ListModel? = null
    private val langReceiver by lazy {
        object: BroadcastReceiver(){
            override fun onReceive(c: Context, i: Intent) {
                Log.d("language", "LANGUAGE CHANGED TO " + Locale.getDefault().getCountry())

                viewModel?.refreshMovie()
                viewModel?.movie(LanguageProvide.getLanguage(c))

                viewModel?.refreshTv()
                viewModel?.tv(LanguageProvide.getLanguage(c))
            }
        }
    }

    override fun onClickMovie(item: MovieData) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(MOVIE_EXTRA, item)
        startActivity(intent)
    }

    override fun onClickTv(item: TvData) {
        val intent = Intent(this, TvDetailsActivity::class.java)
        intent.putExtra(TV_EXTRA, item)
        startActivity(intent)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.movie -> {
                container.setCurrentItem(0, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.tv -> {
                container.setCurrentItem(1, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setupListener(){
        container.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                bottom_navigation.selectedItemId = when(position) {
                    0 -> R.id.movie
                    1 -> R.id.tv
                    else -> 0
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(ListModel::class.java)
        setupLanguage()

        setSupportActionBar(toolbar)
        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val pagerAdapter = PagerAdapter(supportFragmentManager, 2)
        container.adapter = pagerAdapter
        pagerAdapter.notifyDataSetChanged()

        setupListener()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.change_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class PagerAdapter(fragment: FragmentManager, count: Int): FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
        val c = count

        override fun getCount(): Int {
            return c
        }

        override fun getItem(position: Int): Fragment {
            when(position){
                0 -> {
                    return MovieItemFragment()
                }
                1 -> {
                    return TvItemFragment()
                }
                else -> {
                    return Fragment()
                }
            }
        }
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