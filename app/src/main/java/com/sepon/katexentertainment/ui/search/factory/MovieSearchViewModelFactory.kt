package com.sepon.katexentertainment.ui.search.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import com.sepon.katexentertainment.ui.dashboard.ui.DashboardViewModel
import com.sepon.katexentertainment.ui.search.SearchViewModel
import com.sepon.katexentertainment.ui.search.repository.MovieSearchRepository

class MovieSearchViewModelFactory(private val repository: MovieSearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }

}