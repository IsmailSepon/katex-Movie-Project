package com.sepon.katexentertainment.ui.search

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import com.sepon.katexentertainment.ui.search.data.model.ResultsItem
import com.sepon.katexentertainment.ui.search.repository.MovieSearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MovieSearchRepository
)  : ViewModel() {

    private val _movieSearchListData = MutableLiveData<List<ResultsItem?>?>()
    val movieSearchListData: LiveData<List<ResultsItem?>?> = _movieSearchListData


    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    init {

        getMovies()
    }




    fun getMovies() {
        _dataLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMovieListData()
                if (response.isSuccessful && response.code()==200)  {
                    if (response.body()?.results?.size != 0){
                        _movieSearchListData.value = response.body()?.results
                    }
                    _dataLoading.value = false

                } else {
                    _dataLoading.value = false
                }

            } catch (e: Exception) {

            }
        }
    }

}