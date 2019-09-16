package com.app.rachmad.movie.ui.movie

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.app.rachmad.movie.ui.MainActivity
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.BaseActivity
import com.app.rachmad.movie.ui.BaseFragment
import com.app.rachmad.movie.ui.IS_FAVORITE_EXTRA
import com.app.rachmad.movie.ui.movie.search.MovieSearchActivity
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.fragment_movie_item_list.*

class MovieItemFragment : BaseFragment() {
    private var listener: OnMovieClickListener? = null
    lateinit var adapter: MovieItemRecyclerViewAdapter
    var isFavorite = false

    private fun accessData(){
        if(isFavorite){
            if(viewModel.countAllFavoriteMovie() > 0) {
                loading_layout.visibility = ViewGroup.GONE
                movie_loading.visibility = View.GONE
                movie_error.visibility = View.GONE
                list.visibility = ViewGroup.VISIBLE
                movie_refresh.isRefreshing = false
                loading_more.visibility = View.GONE

                var movieFavorite = viewModel.movieFavoriteLiveData
                movieFavorite.observe(this, Observer<PagedList<MovieData>> {
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                })
            }
            else{
                loading_layout.visibility = ViewGroup.VISIBLE
                movie_loading.visibility = View.GONE
                movie_error.visibility = View.VISIBLE
                list.visibility = ViewGroup.GONE
                movie_refresh.isRefreshing = false
                loading_more.visibility = View.GONE

                movie_error.text = getString(R.string.favorite_empty, getString(R.string.movie))
            }
        }
        else {
            val connection = viewModel.connectionMovie()
            viewModel.movie(LanguageProvide.getLanguage(this.context))

            if (list is RecyclerView) {
                connection.observe(this, Observer<Int> {
                    if (statusConnection(it)) {
                        adapter.submitList(viewModel.getMovieList())
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }
    }

    private fun statusConnection(status: Int?): Boolean{
        status?.let {
            when(it){
                Status.LOADING -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    movie_loading.visibility = View.VISIBLE
                    movie_error.visibility = View.GONE
                    list.visibility = ViewGroup.GONE

                    val animator = AnimatorInflater.loadAnimator(this.context, R.animator.fade_out)
                    animator.setTarget(loading_more)
                    animator.setDuration(500)
                    animator.start()
                    return false
                }
                Status.ACCEPTED -> {
                    loading_layout.visibility = ViewGroup.GONE
                    movie_loading.visibility = View.GONE
                    movie_error.visibility = View.GONE
                    list.visibility = ViewGroup.VISIBLE
                    movie_refresh.isRefreshing = false
                    return true
                }
                else -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    movie_loading.visibility = View.GONE
                    movie_error.visibility = View.VISIBLE
                    list.visibility = ViewGroup.GONE
                    movie_refresh.isRefreshing = false
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
        val error = viewModel.errorMovie()
        movie_error.text = error?.status_message ?: run { "" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFavorite = arguments?.getBoolean(IS_FAVORITE_EXTRA, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_item_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        accessData()

        adapter = MovieItemRecyclerViewAdapter(listener)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        movie_refresh.setOnRefreshListener {
            if(!isFavorite) {
                viewModel.refreshMovie()
                movie_refresh.isRefreshing = true
            }
            accessData()
        }

        if(!isFavorite) {
            viewModel.doLoadingMovie().observe(this, Observer<Boolean> {
                val animator = AnimatorInflater.loadAnimator(this.context, if (it)
                    R.animator.fade_in
                else
                    R.animator.fade_out)
                animator.setTarget(loading_more)
                animator.setDuration(500)
                animator.start()
            })
        }

        viewModel.doLoadingMovieSearch().observe(this, Observer<Boolean> {
            val animator = AnimatorInflater.loadAnimator(this.context, if (it)
                R.animator.fade_in
            else
                R.animator.fade_out)
            animator.setTarget(loading_more)
            animator.setDuration(500)
            animator.start()
        })

        search_text.setOnClickListener {
            startActivity(Intent(this.context, MovieSearchActivity::class.java))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMovieClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnMovieClickListener {
        fun onClickMovie(item: MovieData)
    }
}
