package com.chimanos.httpsample

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import com.chimanos.httpsample.global.managers.OMDBManager
import com.chimanos.httpsample.global.models.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    /**
     * Comparaion AsyncTask vs Rx, le code parle de lui même et on comprend pourquoi plus personne n'utilise les AsyncTask,
     * la programmation reactive est devenus un standard dans beaucoup de langage de programmation, ça necéssite beaucoup d'apprentissage car la logique est vraiment
     * particuliére mais écrire une application Java/Kotlin classique ou Android en full reactive c'est un bonheur <3 Bisous
     */

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress)

        val gardienDeLaGalaxyId = "tt3896198"

        //Asynctask
        asynctaskButton.setOnClickListener {
            val movie = OMDBAsyncTask().execute(gardienDeLaGalaxyId).get() //Execute l'asynctask et récupére le résultat avec le .get()
            titleMovie.text = movie!!.Title

            /*
                /!\ Aucune gestion d'erreur ici donc si le film et null du a uen erreur réseau ou autre crash !
             */
        }

        //Rx
        rxButton.setOnClickListener {
            val manager = OMDBManager()
            manager.rxGetMovieById(gardienDeLaGalaxyId)
                .subscribeOn(Schedulers.newThread()) //On subscrit au flux dans un nouveau thread classique
                .observeOn(AndroidSchedulers.mainThread()) //On observe sur le UI THread
                .doOnSubscribe {
                    progressBar.visibility = ProgressBar.VISIBLE //Quand on souscrit l'abonnement au flux on show le loader
                }
                .doOnSuccess {
                    progressBar.visibility = ProgressBar.GONE //Quand c'est en sucess on cache
                }
                .doOnError {
                    progressBar.visibility = ProgressBar.GONE //Quand c'est en error on cache
                }
                .subscribe { it ->
                    titleMovie.text = it.Title //On recupere le résultat
                }
            /*
                /!\ Attention, on a pas gérer le résultat de l'erreur si c'est une erreur on ne passera jamais dans le onSucess du Single mais dans le onError
             */
        }
    }

    private class OMDBAsyncTask : AsyncTask<String, Int, Movie>() { //Premier = type de retour doIn, deuxiéme = type de params de progression & troisiéme type de retour doIn
        override fun doInBackground(vararg ids: String): Movie? {
            val manager = OMDBManager()
            return manager.getMovieById(ids[0]) //Le doIn prend en params un vararg donc ici on prend le 0 car 1 seul id
        }

        fun onProgressUpdate(vararg progress: Int) {
            //Peut gérer la progression si il y a
        }

        fun onPostExecute(result: Long?) {
            //On peut gérer ici la fin de l'operation
            Log.d("AsyncTask", "Finish")
        }
    }
}
