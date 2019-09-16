package com.app.rachmad.movie.ui.tv.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.ui.BaseActivity
import com.app.rachmad.movie.ui.tv.TV_EXTRA
import com.app.rachmad.movie.ui.tv.TvDetailsActivity

class TvSearchActivity : BaseActivity(), TvSearchItemFragment.OnTvSearchListener {
    override fun onListFragmentInteraction(item: TvData) {
        val intent = Intent(this, TvDetailsActivity::class.java)
        intent.putExtra(TV_EXTRA, item)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_search)

        supportFragmentManager.beginTransaction().add(R.id.container, TvSearchItemFragment()).commit()
    }
}
