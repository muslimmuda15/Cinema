package com.app.rachmad.movie.movie

import android.animation.AnimatorInflater
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.app.rachmad.movie.ui.MainActivity
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.tv.TvItemRecyclerViewAdapter
import com.app.rachmad.movie.viewmodel.ListModel
import kotlinx.android.synthetic.main.fragment_tv_item_list.*

class TvItemFragment : Fragment() {
    private var listener: OnTvClickListener? = null
    lateinit var adapter: TvItemRecyclerViewAdapter
    lateinit var viewModel: ListModel

    override fun onResume() {
        super.onResume()

//        accessData()
    }

    private fun accessData(){
        val connection = viewModel.connectionTv()
        viewModel.tv(LanguageProvide.getLanguage(this.context))

        if (list is RecyclerView) {
            connection.observe(this, Observer<Int> {
                if(statusConnection(it)) {
                    adapter.submitList(viewModel.getTvList())
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }

    private fun statusConnection(status: Int?): Boolean{
        status?.let {
            when(it){
                Status.LOADING -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    tv_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    list.visibility = ViewGroup.GONE

                    val animator = AnimatorInflater.loadAnimator(this.context, R.animator.fade_out)
                    animator.setTarget(loading_more)
                    animator.setDuration(500)
                    animator.start()
                    return false
                }
                Status.ACCEPTED -> {
                    loading_layout.visibility = ViewGroup.GONE
                    tv_loading.visibility = View.GONE
                    tv_error.visibility = View.GONE
                    list.visibility = ViewGroup.VISIBLE
                    tv_refresh.isRefreshing = false
                    return true
                }
                else -> {
                    loading_layout.visibility = ViewGroup.VISIBLE
                    tv_loading.visibility = View.GONE
                    tv_error.visibility = View.VISIBLE
                    list.visibility = ViewGroup.GONE
                    tv_refresh.isRefreshing = false
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
        val error = viewModel.errorTv()
        tv_error.text = error?.status_message ?: run { getString(R.string.unknown_error) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_item_list, container, false)
        viewModel = (activity as MainActivity).viewModel!!
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = TvItemRecyclerViewAdapter(listener)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        accessData()

        tv_refresh.setOnRefreshListener {
            viewModel.refreshTv()
            accessData()
            tv_refresh.isRefreshing = true
        }

        viewModel.doLoadingTv().observe(this, Observer<Boolean> {
            val animator = AnimatorInflater.loadAnimator(this.context, if(it)
                R.animator.fade_in
            else
                R.animator.fade_out)
            animator.setTarget(loading_more)
            animator.setDuration(500)
            animator.start()
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTvClickListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnTvClickListener {
        fun onClickTv(item: TvData)
    }
}
