package com.sepon.katexentertainment.ui.dashboard.data.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.babu.smartlock.sessionManager.UserSessionManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sepon.katexentertainment.R
import com.sepon.katexentertainment.databinding.MovieItemBinding
import com.sepon.katexentertainment.databinding.StarMovieItemBinding
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import java.lang.reflect.Type


class StarMoviesAdapter (
    private val movies: ArrayList<StarModel?>?,
    mContext: Context,
//    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<StarMoviesAdapter.JobsViewHolder>(){

    private val mContext = mContext
    override fun getItemCount() = movies!!.size
    val userSession = UserSessionManager(mContext)


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
            val list = getCompaniesList()
            list.removeAt(position)
            setList(list)
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

    private fun <T> setList(list: ArrayList<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        userSession.favoritMovies = json
    }

    fun getCompaniesList(): ArrayList<StarModel> {
        if (userSession.favoritMovies != "") {
            val gson = Gson()
            val companyList: ArrayList<StarModel>
            val string: String? = userSession.favoritMovies//.getString(key, null)
            val type: Type = object : TypeToken<ArrayList<StarModel?>?>() {}.getType()
            companyList = gson.fromJson<ArrayList<StarModel>>(string, type)
            return companyList
        }
        return ArrayList<StarModel>()
    }
}