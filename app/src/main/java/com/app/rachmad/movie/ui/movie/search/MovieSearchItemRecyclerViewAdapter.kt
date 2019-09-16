package com.app.rachmad.movie.ui.movie.search

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.MovieData
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_movie_search_item.view.*

class MovieSearchItemRecyclerViewAdapter(
        private val mListener: MovieSearchItemFragment.OnMovieSearchListener?)
    : PagedListAdapter<MovieData, MovieSearchItemRecyclerViewAdapter.ViewHolder>(checkDifferent) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as MovieData
            mListener?.onMovieSearch(item)
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
                .inflate(R.layout.fragment_movie_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            GlideApp.with(holder.image)
                    .load(BuildConfig.IMAGE_URL + item.poster_path)
                    .fitCenter()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            holder.imageLoading.stopShimmer()
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            holder.imageLoading.stopShimmer()
                            holder.imageLoading.setShimmer(null)
                            return false
                        }
                    })
                    .into(holder.image)

            with(holder) {
                title.text = item.title
                overview.text = if (item.overview.isBlank())
                    androidx.core.text.HtmlCompat.fromHtml("<i>${itemView.context.getString(com.app.rachmad.movie.R.string.no_preview)}</i>", androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY)
                else
                    item.overview

                var df = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                val newDate = df.parse(item.release_date)
                android.util.Log.d("date", item.release_date + " -> " + newDate.toString())
                df = java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.US)
                date.text = df.format(newDate)

                ratingStar.rating = item.vote_average / 2
                ratingText.text = item.vote_average.toString()
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
        val ratingText = mView.rate_text
        val imageLoading = mView.image_layout
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
