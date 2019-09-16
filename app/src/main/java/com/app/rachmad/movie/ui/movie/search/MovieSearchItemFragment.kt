package com.app.rachmad.movie.ui.movie.search

import android.animation.AnimatorInflater
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.app.rachmad.movie.App
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_movie_search_item_list.*
import java.util.*

class MovieSearchItemFragment : BaseFragment() {
    private var listener: OnMovieSearchListener? = null
    lateinit var adapter: MovieSearchItemRecyclerViewAdapter

    private fun accessData(){
        val connection = viewModel.connectionMovieSearch()
        connection.observe(this, Observer<Int>{
            if(statusConnection(it)){
                adapter.submitList(viewModel.getMovieSearch())
                adapter.notifyDataSetChanged()
            }
        })
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
                    return true
                }
                else -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    movie_loading.visibility = View.GONE
                    movie_error.visibility = View.VISIBLE
                    list.visibility = ViewGroup.GONE
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
        val error = viewModel.errorMovieSearch()
        movie_error.text = error?.status_message ?: run { "" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_movie_search_item_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loading_layout.visibility = ViewGroup.GONE
        movie_loading.visibility = View.GONE
        movie_error.visibility = View.GONE
        list.visibility = ViewGroup.GONE
        loading_more.visibility = View.GONE

        adapter = MovieSearchItemRecyclerViewAdapter(listener)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        accessData()

        search_text.addTextChangedListener(object: TextWatcher{
            var timer = Timer()
            val DELAY: Long = 700

            override fun afterTextChanged(s: Editable?) {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        if(s.toString().length > 2) {
                            viewModel.movieSearch(s.toString(), LanguageProvide.getLanguage(c))
                            viewModel.refreshMovieSearch()
                            loading_more.visibility = View.VISIBLE
                        }
                    }
                }, DELAY)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMovieSearchListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnMovieSearchListener {
        fun onMovieSearch(item: MovieData)
    }
}