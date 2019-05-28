package com.chimanos.httpsample.global.managers

import com.chimanos.httpsample.global.models.Movie
import com.chimanos.httpsample.global.services.OMDBService
import com.chimanos.httpsample.global.utils.ConstantKey.Companion.API_KEY
import com.chimanos.httpsample.global.utils.ConstantKey.Companion.BASE_URL
import io.reactivex.Single
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class OMDBManager {

    private lateinit var retrofit: Retrofit
    private val okHttpClient: OkHttpClient
    private val interceptor: HttpLoggingInterceptor

    init {
        interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    /**
     * Execute et retourne la requette
     */
    fun getMovieById(id: String): Movie? {
        //C'est très mal d'initiliser à chaque fois Retrofit sur chaque appel, a ne jamais faire mais ici necessaire pour exemple "normal" et "rx"
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) //Depuis mes constantes la base URL qui ressemble toujours à quelque chose comme: http://baseurl.com/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OMDBService::class.java)
        return service.getMovieInfos(id, API_KEY).execute().body()
    }

    fun rxGetMovieById(id: String): Single<Movie> {
        //C'est très mal d'initiliser à chaque fois Retrofit sur chaque appel, a ne jamais faire mais ici necessaire pour exemple "normal" et "rx"
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) //Depuis mes constantes la base URL qui ressemble toujours à quelque chose comme: http://baseurl.com/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //On ajoute l'adapter pour rx
            .build()

        val service = retrofit.create(OMDBService::class.java)
        return service.rxGetMovieInfos(id, API_KEY)
    }
}