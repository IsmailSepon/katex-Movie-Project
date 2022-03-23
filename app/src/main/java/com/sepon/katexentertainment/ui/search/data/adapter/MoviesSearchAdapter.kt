package com.sepon.katexentertainment.ui.search.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sepon.katexentertainment.R
import com.sepon.katexentertainment.databinding.MovieItemBinding
import com.sepon.katexentertainment.databinding.SearchMovieItemBinding
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.search.data.model.ResultsItem


class MoviesSearchAdapter (
    private val movies: List<ResultsItem?>?,
    mContext: Context,
    val clickListener: ClickListener
) : RecyclerView.Adapter<MoviesSearchAdapter.JobsViewHolder>() {

    private val mContext = mContext
    override fun getItemCount() = movies!!.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        JobsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.search_movie_item,
                parent,
                false
            )
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.bind(movies!![position]!!, clickListener)
        holder.recyclerviewJobBinding.searchmovie = movies[position]

        if (movies[position]?.image != null) {
            Glide.with(mContext)
                .load(movies[position]?.image!!)
                .into(holder.recyclerviewJobBinding.roundedImageView);
        }

    }


    inner class JobsViewHolder(
        val recyclerviewJobBinding: SearchMovieItemBinding
    ) : RecyclerView.ViewHolder(recyclerviewJobBinding.root){
        fun bind(movie: ResultsItem, clickListener: ClickListener) {
            recyclerviewJobBinding.searchmovie = movie
            recyclerviewJobBinding.executePendingBindings()
            recyclerviewJobBinding.clickListener = clickListener
        }
    }



}

    class ClickListener(val clickListener: (questionData : ResultsItem) -> Unit) {
        fun onClick(questionData: ResultsItem) {
            clickListener(questionData)
        }
}