package com.idd.amazonchallenge.application

import android.app.Application
import com.idd.amazonchallenge.di.actionsModule
import com.idd.amazonchallenge.di.repositoriesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
class AmazonChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AmazonChallengeApplication)
            modules(
                listOf(
                    repositoriesModule,
                    actionsModule
                )
            )
        }
    }
}
