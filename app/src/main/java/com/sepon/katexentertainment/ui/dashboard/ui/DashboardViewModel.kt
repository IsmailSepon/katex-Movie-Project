package com.sepon.katexentertainment.ui.dashboard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepon.katexentertainment.ui.dashboard.data.model.ItemsItem
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieItem
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: MovieListRepository
)  : ViewModel() {



    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    private val _movieListData = MutableLiveData<List<ItemsItem?>?>()
    val movieListData: LiveData<List<ItemsItem?>?> = _movieListData

    private val _starmovieListData = MutableLiveData<List<StarModel?>?>()
    val starMovieListData: LiveData<List<StarModel?>?> = _starmovieListData



    init {

        getMovies()
        getStrtMovie()
    }


    fun getStrtMovie(){
        viewModelScope.launch {
            try {
                val response = repository.getstarMovieListData()
                _starmovieListData.value = response

            } catch (e: Exception) {

            }
        }
    }

    fun getMovies() {
        _dataLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getMovieListData()
                if (response.isSuccessful && response.code()==200)  {
                    _movieListData.value = response.body()?.items
                    _dataLoading.value = false


                } else {
                    _dataLoading.value = false
                }

            } catch (e: Exception) {

            }
        }
    }







}