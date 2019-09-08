package com.app.rachmad.movie.ui.movie

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.app.rachmad.movie.viewmodel.ListModel

import kotlinx.android.synthetic.main.fragment_movie_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovieItemRecyclerViewAdapter(
        private val viewModel: ListModel,
        private val mListener: MovieItemFragment.OnMovieClickListener?)
    : PagedListAdapter<MovieData, MovieItemRecyclerViewAdapter.ViewHolder>(checkDifferent) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as MovieData
            mListener?.onClickMovie(item)
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAppear()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDisappear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            GlideApp.with(holder.image)
                    .load(BuildConfig.IMAGE_URL + item.poster_path)
                    .fitCenter()
                    .into(holder.image)

            with(holder) {
                title.text = item.title
                overview.text = if(item.overview.isNullOrBlank())
                    HtmlCompat.fromHtml("<i>${itemView.context.getString(R.string.no_preview)}</i>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                else
                    item.overview

                var df = SimpleDateFormat("yyyy-mm-dd", Locale.US)
                val newDate = df.parse(item.release_date)
                df = SimpleDateFormat("MMM yyyy", Locale.US)
                date.text = df.format(newDate)

                ratingStar.rating = item.vote_average / 2
            }

            with(holder.mView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView), LifecycleOwner {
        private val lifeCycleRegistry = LifecycleRegistry(this)
        init {
            lifeCycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }
        fun onAppear(){
            lifeCycleRegistry.currentState = Lifecycle.State.CREATED
        }
        fun onDisappear(){
            lifeCycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
        override fun getLifecycle(): Lifecycle {
            return lifeCycleRegistry
        }

        val image = mView.image
        val title = mView.title
        val overview = mView.overview
        val date = mView.date
        val ratingStar = mView.rating_star
    }

    companion object {
        val checkDifferent = object : DiffUtil.ItemCallback<MovieData>() {
            override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData): Boolean =
                    oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData): Boolean =
                    oldItem == newItem
        }
    }
}
