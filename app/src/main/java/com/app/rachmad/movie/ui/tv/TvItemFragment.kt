package com.app.rachmad.movie.ui.tv

import android.animation.AnimatorInflater
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.helper.LanguageProvide
import com.app.rachmad.movie.helper.Status
import com.app.rachmad.movie.ui.BaseFragment
import com.app.rachmad.movie.ui.IS_FAVORITE_EXTRA
import kotlinx.android.synthetic.main.fragment_tv_item_list.*

class TvItemFragment : BaseFragment() {
    private var listener: OnTvClickListener? = null
    lateinit var adapter: TvItemRecyclerViewAdapter
    var isFavorite = false

    private fun accessData(){
        if(isFavorite){
            if(viewModel.countAllFavoriteTv() > 0) {
                loading_layout.visibility = ViewGroup.GONE
                tv_loading.visibility = View.GONE
                tv_error.visibility = View.GONE
                list.visibility = ViewGroup.VISIBLE
                tv_refresh.isRefreshing = false
                loading_more.visibility = View.GONE

                val tvFavorite = viewModel.tvFavoriteLiveData
                tvFavorite.observe(this, Observer<PagedList<TvData>> {
                    adapter.submitList(it)
                    adapter.notifyDataSetChanged()
                })
            }
            else{
                loading_layout.visibility = ViewGroup.VISIBLE
                tv_loading.visibility = View.GONE
                tv_error.visibility = View.VISIBLE
                list.visibility = ViewGroup.GONE
                tv_refresh.isRefreshing = false
                loading_more.visibility = View.GONE

                tv_error.text = getString(R.string.favorite_empty, getString(R.string.tv))
            }
        }
        else {
            val connection = viewModel.connectionTv()
            viewModel.tv(LanguageProvide.getLanguage(this.context))

            if (list is RecyclerView) {
                connection.observe(this, Observer<Int> {
                    if (statusConnection(it)) {
                        adapter.submitList(viewModel.getTvList())
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        }
    }

    private fun onSearch(){
        if(isFavorite){
            search_layout.visibility = ViewGroup.GONE
        }
        else {
            search_layout.visibility = ViewGroup.VISIBLE
            val connectionSearch = viewModel.connectionTvSearch()
            connectionSearch.observe(this, Observer<Int> {
                if(statusConnection(it)){
                    adapter.submitList(viewModel.getTvSearch())
                    adapter.notifyDataSetChanged()
                }
            })

            search_text.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if(s.toString() == "") {
                        loading_layout.visibility = ViewGroup.GONE
                        tv_loading.visibility = View.GONE
                        tv_error.visibility = View.GONE
                        list.visibility = ViewGroup.VISIBLE
                        tv_refresh.isRefreshing = false

                        adapter.submitList(viewModel.getTvList())
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            })

            search_text.setOnEditorActionListener { textView, i, keyEvent ->
                if(i == EditorInfo.IME_ACTION_SEARCH){
                    viewModel.refreshMovieSearch()
                    viewModel.movieSearch(search_text.text.toString(), LanguageProvide.getLanguage(context))
                    true
                }
                false
            }

            search_text.setOnTouchListener { view, motionEvent ->
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (motionEvent.rawX >= (search_text.right - search_text.compoundDrawables[DRAWABLE_RIGHT].bounds.width() - resources.getDimension(R.dimen.double_margin))) {
                        // TODO: Close Button
                        search_text.setText("")

                        loading_layout.visibility = ViewGroup.GONE
                        tv_loading.visibility = View.GONE
                        tv_error.visibility = View.GONE
                        list.visibility = ViewGroup.VISIBLE
                        tv_refresh.isRefreshing = false

                        adapter.submitList(viewModel.getTvList())
                        adapter.notifyDataSetChanged()
                        true
                    }
                    if (motionEvent.rawX <= (search_text.compoundDrawables[DRAWABLE_LEFT].bounds.width() + resources.getDimension(R.dimen.double_margin))) {
                        // TODO: Search Button
                        viewModel.refreshTvSearch()
                        viewModel.tvSearch(search_text.text.toString(), LanguageProvide.getLanguage(context))
                        true
                    }
                }
                false
            }
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
        val error = if(search_text.text.toString().isBlank())
            viewModel.errorTv()
        else
            viewModel.errorTvSearch()

        if(error?.status_code == Status.EMPTY_DATA)
            tv_error.text = getString(R.string.empty_data)
        else
            tv_error.text = error?.status_message ?: run { "" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFavorite = arguments?.getBoolean(IS_FAVORITE_EXTRA, false) ?: false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tv_item_list, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = TvItemRecyclerViewAdapter(listener)
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = adapter

        accessData()

        tv_refresh.setOnRefreshListener {
            if(!isFavorite) {
                tv_refresh.isRefreshing = true
                if(search_text.text.toString().isBlank()) {
                    viewModel.refreshTv()
                    accessData()
                }
                else{
                    viewModel.refreshTvSearch()
                    viewModel.tvSearch(search_text.text.toString(), LanguageProvide.getLanguage(context))
                }
            }
            else {
                accessData()
            }
        }

        if(!isFavorite) {
            viewModel.doLoadingTv().observe(this, Observer<Boolean> {
                val animator = AnimatorInflater.loadAnimator(this.context, if (it)
                    R.animator.fade_in
                else
                    R.animator.fade_out)
                animator.setTarget(loading_more)
                animator.setDuration(500)
                animator.start()
            })
        }

        viewModel.doLoadingTvSearch().observe(this, Observer<Boolean> {
            val animator = AnimatorInflater.loadAnimator(this.context, if (it)
                R.animator.fade_in
            else
                R.animator.fade_out)
            animator.setTarget(loading_more)
            animator.setDuration(500)
            animator.start()
        })

        onSearch()
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
