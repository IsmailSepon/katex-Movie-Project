package com.sepon.katexentertainment.ui.dashboard.factory

import android.app.Application
import androidx.fragment.app.Fragment
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import com.sepon.katexentertainment.ui.search.factory.MovieSearchViewModelFactory
import com.sepon.katexentertainment.ui.search.repository.MovieSearchRepository

object ViewModelFactoryUtil {



    fun provideMovieListFactory(fragment: Fragment) : MovieListViewModelFactory {
        val repository = MovieListRepository(fragment.requireContext().applicationContext as Application)
        return MovieListViewModelFactory(repository)
    }


    fun provideMovieSearchListFactory(fragment: Fragment) : MovieSearchViewModelFactory {
        val repository = MovieSearchRepository(fragment.requireContext().applicationContext as Application)
        return MovieSearchViewModelFactory(repository)
    }


}