package com.idd.amazonchallenge.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.repositories.RedditLocalRepository
import com.idd.infrastructure.repositories.RedditLocalRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
val repositoriesModule = module {
    single<RedditLocalRepository> {
        RedditLocalRepositoryImpl(
            androidContext(),
            provideSharedPreferences(androidContext()),
            Gson()
        )
    }
}

val actionsModule = module {
    single { GetLocalRedditEntriesAction(get()) }
}

private fun provideSharedPreferences(app: Context): SharedPreferences =
    app.getSharedPreferences(RedditLocalRepositoryImpl.USER_PREFERENCES, Context.MODE_PRIVATE)

fun provideGSon(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}