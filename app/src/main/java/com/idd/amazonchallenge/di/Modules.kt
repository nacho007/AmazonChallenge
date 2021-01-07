package com.idd.amazonchallenge.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.idd.amazonchallenge.ui.details.DetailViewModel
import com.idd.amazonchallenge.ui.list.ListViewModel
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.actions.SavePictureAction
import com.idd.domain.repositories.RedditLocalRepository
import com.idd.domain.repositories.SavePictureRepository
import com.idd.infrastructure.repositories.RedditLocalRepositoryImpl
import com.idd.infrastructure.repositories.SavePictureRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by ignaciodeandreisdenis on 1/6/21.
 */
val repositoriesModule = module {
    single<RedditLocalRepository> {
        RedditLocalRepositoryImpl(
            androidContext(),
            provideSharedPreferences(androidContext()),
            provideGSon()
        )
    }
    single<SavePictureRepository> { SavePictureRepositoryImpl(androidContext()) }
}

val viewModelsModule = module {
    viewModel { ListViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

val actionsModule = module {
    single { GetLocalRedditEntriesAction(get()) }
    single { SavePictureAction(get()) }
}

private fun provideSharedPreferences(app: Context): SharedPreferences =
    app.getSharedPreferences(RedditLocalRepositoryImpl.USER_PREFERENCES, Context.MODE_PRIVATE)

fun provideGSon(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}