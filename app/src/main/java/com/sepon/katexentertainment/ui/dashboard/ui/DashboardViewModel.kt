package com.sepon.katexentertainment.ui.dashboard.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sepon.katexentertainment.ui.dashboard.data.MovieViewModel
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieItem
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieListResponse
import com.sepon.katexentertainment.ui.dashboard.data.repository.MovieListRepository
import com.sepon.katexentertainment.ui.dashboard.others.Event
import com.sepon.katexentertainment.ui.dashboard.others.MovieListEvent
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: MovieListRepository
)  : ViewModel() {



    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


    private val _movieListData = MutableLiveData<List<MovieItem?>?>()
    val movieListData: LiveData<List<MovieItem?>?> = _movieListData



    init {
        getQuestions()
    }


    fun getQuestions() {
        _dataLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getMovieListData()
                if (response.isSuccessful && response.code()==200)  {
                    _movieListData.value = response.body()?.movies
                    _dataLoading.value = false
                } else {
                    _dataLoading.value = false
                }

            } catch (e: Exception) {

            }
        }
    }


//    val data: LiveData<List<MovieViewModel>>
//        get() = _data
//    private val _data = MutableLiveData<List<MovieViewModel>>(emptyList())
//
//    val events: LiveData<Event<MovieListEvent>>
//        get() = _events
//    private val _events = MutableLiveData<Event<MovieListEvent>>()
//
//    init {
//        loadData()
//    }
//
//    private fun loadData() {
//        viewModelScope.launch {
//            val carList = movieDataProvider.getMovieListData()
//
////            val carsByMake = carList.groupBy { it.make }
////
////            val viewData = createViewData(carsByMake)
////            _data.postValue(viewData)
//        }
//    }
//
//    private fun createViewData(carsByMake: Map<String, List<CarData>>): List<MovieViewModel> {
//        val viewData = mutableListOf<MovieViewModel>()
//        carsByMake.keys.forEach {
//            viewData.add(HeaderViewModel(it))
//            val cars = carsByMake[it]
//            cars?.forEach { car: CarData ->
//                val item = if (car.isAd) {
//                    CarAdViewModel(car.make, car.model, car.price)
//                } else {
//                    CarListingViewModel(car.make, car.model, car.price, ::onCarListingClicked)
//                }
//                viewData.add(item)
//            }
//        }
//
//        return viewData
//    }
//
//    private fun onCarListingClicked(carDetails: String) {
//        _events.postValue(Event(MovieListEvent.ShowSelectedCar(carDetails)))
//    }
//
//    companion object {
//        const val HEADER_ITEM = 0
//        const val LISTING_ITEM = 1
//        const val AD_ITEM = 2
//    }
}