package com.sepon.katexentertainment.ui.dashboard.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import com.sepon.katexentertainment.ui.dashboard.ui.DashboardViewModel

class MovieListViewModelFactory(private val repository: MovieListRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(repository) as T
    }

}