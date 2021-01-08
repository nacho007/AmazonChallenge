package com.idd.amazonchallenge.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.idd.amazonchallenge.ui.details.DetailViewModel
import com.idd.amazonchallenge.ui.list.ListViewModel
import com.idd.domain.actions.GetLocalRedditEntriesAction
import com.idd.domain.actions.GetNetWorkRedditEntriesAction
import com.idd.domain.actions.SavePictureAction
import com.idd.domain.repositories.RedditLocalRepository
import com.idd.domain.repositories.RedditNetworkRepository
import com.idd.domain.repositories.SavePictureRepository
import com.idd.infrastructure.clients.RedditClient
import com.idd.infrastructure.network.ResponseHandler
import com.idd.infrastructure.repositories.RedditLocalRepositoryImpl
import com.idd.infrastructure.repositories.RedditNetworkRepositoryImpl
import com.idd.infrastructure.repositories.SavePictureRepositoryImpl
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    single<RedditNetworkRepository> {
        RedditNetworkRepositoryImpl(
            get(),
            get(),
            provideSharedPreferences(androidContext()),
            Gson()
        )
    }
    single<SavePictureRepository> { SavePictureRepositoryImpl(androidContext()) }
}

val viewModelsModule = module {
    viewModel { ListViewModel(get(), get()) }
    viewModel { DetailViewModel(get()) }
}

val actionsModule = module {
    single { GetLocalRedditEntriesAction(get()) }
    single { GetNetWorkRedditEntriesAction(get()) }
    single { SavePictureAction(get()) }
}

val netWorkModule = module {
    factory { ResponseHandler(Dispatchers.IO) }

    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideRequestInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()

                val url = request.url.newBuilder()
                    .build()

                val newRequest = request.newBuilder()
                    .url(url)
                    .build()

                return chain.proceed(newRequest)
            }
        }
    }

    fun provideHttpClient(cache: Cache, interceptor: Interceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(logging)
            .cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.reddit.com/")
            .addConverterFactory(GsonConverterFactory.create(factory))
            .client(client)
            .build()
    }

    fun provideRedditClient(retrofit: Retrofit): RedditClient =
        retrofit.create(RedditClient::class.java)

    fun provideGSon(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    single { provideCache(androidApplication()) }
    single { provideHttpClient(get(), provideRequestInterceptor()) }
    single { provideGSon() }
    single { provideRetrofit(get(), get()) }
    single { provideRedditClient(get()) }
}

private fun provideSharedPreferences(app: Context): SharedPreferences =
    app.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
