package com.sepon.katexentertainment.ui.dashboard.data.adapter

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
import com.sepon.katexentertainment.databinding.StarMovieItemBinding
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel


class StarMoviesAdapter (
    private val movies: ArrayList<StarModel?>?,
    mContext: Context,
//    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<StarMoviesAdapter.JobsViewHolder>(){

    private val mContext = mContext
    override fun getItemCount() = movies!!.size



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        JobsViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.star_movie_item, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {

        holder.recyclerviewJobBinding.starmovie = movies!![position]

        if (movies[position]?.image != null){
            Glide.with(mContext)
                .load(movies[position]?.image!!)
                .into(holder.recyclerviewJobBinding.roundedImageView);
        }

        holder.recyclerviewJobBinding.imageView.setOnClickListener{
            movies.removeAt(position)
            notifyDataSetChanged()
        }


    }




    inner class JobsViewHolder(
        val recyclerviewJobBinding: StarMovieItemBinding
    ) : RecyclerView.ViewHolder(recyclerviewJobBinding.root)



    interface RecyclerViewClickListener {
        fun onRecyclerViewItemClick(view: View, movie: ItemsItem)
    }
}