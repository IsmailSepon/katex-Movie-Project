package com.sepon.katexentertainment.ui.dashboard.factory

import android.app.Application
import androidx.fragment.app.Fragment
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository

object ViewModelFactoryUtil {



    fun provideMovieListFactory(fragment: Fragment) : MovieListViewModelFactory {
        val repository = MovieListRepository(fragment.requireContext().applicationContext as Application)
        return MovieListViewModelFactory(repository)
    }


}