package com.sepon.katexentertainment.ui.dashboard.data.repository

import android.app.Application
import com.babu.smartlock.sessionManager.UserSessionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sepon.katexentertainment.ui.dashboard.data.model.GetMovieResponse
import com.sepon.katexentertainment.ui.dashboard.data.model.StarModel
import com.sepon.katexentertainment.ui.dashboard.data.remote.MovieApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.reflect.Type

class MovieListRepository(private val application: Application) {

    suspend fun getMovieListData(): Response<GetMovieResponse> {
        return withContext(Dispatchers.IO) {
            MovieApiService.create(application.applicationContext).getMoviesList()
        }
    }

     fun getstarMovieListData():  List<StarModel>  {


          val userSession = UserSessionManager(application.applicationContext)

             val gson = Gson()
             val starMovies: ArrayList<StarModel>
             val string: String? = userSession.favoritMovies//.getString(key, null)
             val type: Type = object : TypeToken<ArrayList<StarModel?>?>() {}.type
            starMovies = gson.fromJson<ArrayList<StarModel>>(string, type)
             return starMovies.toList()


//        val starMovies = arrayOf(
//            StarModel("https://m.media-amazon.com/images/M/MV5BMDdmMTBiNTYtMDIzNi00NGVlLWIzMDYtZTk3MTQ3NGQxZGEwXkEyXkFqcGdeQXVyMzMwOTU5MDk@._V1_UX128_CR0,3,128,176_AL_.jpg", "The Batman", "The Batman (2022)"),
//            StarModel("https://m.media-amazon.com/images/M/MV5BOWM0YWMwMDQtMjE5NS00ZTIwLWE1NWEtODViMWZjMWI2OTU3XkEyXkFqcGdeQXVyMTEyMjM2NDc2._V1_UX128_CR0,3,128,176_AL_.jpg", "The Adam Project", "The Adam Project (2022)")
//        )

//        return starMovies.toList()
    }

}