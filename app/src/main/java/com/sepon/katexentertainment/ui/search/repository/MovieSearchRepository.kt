package com.sepon.katexentertainment.ui.search.repository

import android.app.Application
import com.sepon.katexentertainment.ui.dashboard.data.remote.MovieApiService
import com.sepon.katexentertainment.ui.search.data.model.SearchResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieSearchRepository(private val application: Application) {

    suspend fun getMovieListData(key : String): Response<SearchResponse> {
        return withContext(Dispatchers.IO) {
            MovieApiService.create(application.applicationContext).getMoviesSearch(key)
        }
    }

}