package com.sepon.katexentertainment.ui.dashboard.others

sealed class MovieListEvent {
    data class ShowSelectedCar(val carDetails: String) : MovieListEvent()
}
