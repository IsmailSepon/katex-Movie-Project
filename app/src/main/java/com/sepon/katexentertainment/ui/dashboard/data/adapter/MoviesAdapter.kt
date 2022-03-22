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
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem


class MoviesAdapter (
    private val movies: List<ItemsItem?>?,
    mContext: Context,
//    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<MoviesAdapter.JobsViewHolder>(){

    private val mContext = mContext
    override fun getItemCount() = movies!!.size
    val imageBaseUrl : String = "https://image.tmdb.org/t/p/w500"



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        JobsViewHolder(DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.movie_item, parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {

        holder.recyclerviewJobBinding.movie = movies!![position]

        if (movies[position]?.image != null){
            Glide.with(mContext)
                .load(movies[position]?.image!!)
                .into(holder.recyclerviewJobBinding.roundedImageView);
        }


        holder.itemView.setOnClickListener{
            //listener.onRecyclerViewItemClick(it, movies[position]!!)
        }


    }




    inner class JobsViewHolder(
        val recyclerviewJobBinding: MovieItemBinding
    ) : RecyclerView.ViewHolder(recyclerviewJobBinding.root)



    interface RecyclerViewClickListener {
        fun onRecyclerViewItemClick(view: View, movie: ItemsItem)
    }
}