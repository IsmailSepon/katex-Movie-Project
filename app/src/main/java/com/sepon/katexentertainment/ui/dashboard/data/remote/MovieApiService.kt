package com.sepon.katexentertainment.ui.dashboard.data.remote

import android.content.Context
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.sepon.katexentertainment.ui.dashboard.data.model.MovieListResponse
import com.sepon.katexentertainment.ui.dashboard.util.NetworkConnectionInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit


private const val MOVIE_BASE_URL = "https://imdb-api.com/en/API/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

interface MovieApiService {


    @FormUrlEncoded
    @POST("Top250Movies/k_i6f1t24j")
    suspend fun getMoviesList(): Response<MovieListResponse>



    companion object Factory {
        @Volatile
        private var retrofit: Retrofit? = null

        @Synchronized
        fun create(context: Context): MovieApiService {

            retrofit ?: synchronized(this) {
                retrofit = buildRetrofit(context)
            }

            return retrofit?.create(MovieApiService::class.java)!!
        }



        private fun buildRetrofit(context: Context): Retrofit {

            val loginOkHttpClient = OkHttpClient.Builder()
                    .addInterceptor(NetworkConnectionInterceptor(context))
                    .addInterceptor(OkHttpProfilerInterceptor())
                    .build()

            return Retrofit.Builder().apply {
                addConverterFactory(MoshiConverterFactory.create(moshi))
                baseUrl(MOVIE_BASE_URL)
                client(loginOkHttpClient)
            }.build()
        }

    }

}
