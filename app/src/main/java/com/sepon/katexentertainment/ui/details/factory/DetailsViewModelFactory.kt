package com.sepon.katexentertainment.ui.details.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sepon.katexentertainment.ui.details.DetailsViewModel
import com.sepon.katexentertainment.ui.search.repository.MovieSearchRepository

class DetailsViewModelFactory(private val repository: MovieSearchRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository) as T
    }

}