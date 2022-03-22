package com.sepon.katexentertainment.ui.dashboard.data.repository

import android.app.Application
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieItem
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieListResponse
import com.sepon.katexentertainment.ui.dashboard.data.remote.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieListRepository(private val application: Application) {

    suspend fun getMovieListData(): Response<MovieListResponse> {
        return withContext(Dispatchers.IO) {
            MovieApiService.create(application.applicationContext).getMoviesList()
        }
    }
}