package com.app.rachmad.movie.ui.tv

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.app.rachmad.movie.BuildConfig
import com.app.rachmad.movie.GlideApp
import com.app.rachmad.movie.R
import com.app.rachmad.movie.`object`.TvData
import com.app.rachmad.movie.movie.TvItemFragment
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import kotlinx.android.synthetic.main.fragment_tv_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class TvItemRecyclerViewAdapter(
        private val mListener: TvItemFragment.OnTvClickListener?)
    : PagedListAdapter<TvData, TvItemRecyclerViewAdapter.ViewHolder>(checkDifferent) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as TvData
            mListener?.onClickTv(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_tv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { item ->
            GlideApp.with(holder.image)
                    .load(BuildConfig.IMAGE_URL + item.poster_path)
                    .fitCenter()
                    .listener(object: RequestListener<Drawable> {
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
                title.text = item.name
                overview.text = if(item.overview.isNullOrBlank())
                    HtmlCompat.fromHtml("<i>${itemView.context.getString(R.string.no_preview)}</i>", HtmlCompat.FROM_HTML_MODE_LEGACY)
                else
                    item.overview

                var df = SimpleDateFormat("yyyy-mm-dd", Locale.US)
                val newDate = df.parse(item.first_air_date)
                df = SimpleDateFormat("MMM yyyy", Locale.US)
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

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val image = mView.image
        val title = mView.title
        val overview = mView.overview
        val date = mView.date
        val ratingStar = mView.rating_star
        val ratingText = mView.rate_text
        val imageLoading = mView.image_layout
    }

    companion object {
        val checkDifferent = object : DiffUtil.ItemCallback<TvData>() {
            override fun areItemsTheSame(oldItem: TvData, newItem: TvData): Boolean =
                    oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: TvData, newItem: TvData): Boolean =
                    oldItem == newItem
        }
    }
}
