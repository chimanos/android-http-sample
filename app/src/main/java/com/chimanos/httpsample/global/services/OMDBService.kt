package com.chimanos.httpsample.global.services

import com.chimanos.httpsample.global.models.Movie
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBService {
    //Exemple d'appel OMDB: http://www.omdbapi.com/?i=tt3896198&apikey=3bfdcb0a
    //@Query si c'est uen query dans l'url sinon @Path si c'est une valeur dans l'URL

    /**
     * Method normal sans RxJava / RxAndroid
     */
    @GET(".") //On utilise . si la base url est la même que pour tout la requete (pas de /qqchose)
    fun getMovieInfos(@Query("i") i: String, @Query("apikey") apikey: String): Call<Movie>

    /**
     * Avec RxJava / RxAndroid
     */
    @GET(".") //On utilise . si la base url est la même que pour tout la requete (pas de /qqchose)
    fun rxGetMovieInfos(@Query("i") i: String, @Query("apikey") apikey: String): Single<Movie> //On renvoie un Single car Observable(Flux) avec 1 seul item
}