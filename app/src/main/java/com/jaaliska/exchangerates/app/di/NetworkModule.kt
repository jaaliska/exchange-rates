package com.jaaliska.exchangerates.app.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.jaaliska.exchangerates.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


internal val provideRetrofits: Module.() -> Unit = {
    single<OkHttpClient> { provideOkHttpClient() }
    single<Retrofit> { provideRetrofit(client = get(), baseUrl = BuildConfig.SERVER_URL) }
}

private const val TIMEOUT = 100L
private fun provideOkHttpClient(): OkHttpClient {
    val dispatcher = Dispatcher().apply {
        maxRequests = 5
    }
    return with(OkHttpClient.Builder()) {
        dispatcher(dispatcher)
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        addInterceptor(CustomInterceptor())
        addInterceptor(provideLoggingInterceptor())
        build()
    }
}

class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(BuildConfig.API_KEY_NAME, BuildConfig.API_KEY_VALUE)
            .build()
        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)
        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}

private fun provideRetrofit(
    client: OkHttpClient,
    baseUrl: String
): Retrofit {
    val gsonFactory = GsonConverterFactory.create(GsonBuilder().setLenient().create())
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(gsonFactory)
        .build()
}

private fun provideLoggingInterceptor(): Interceptor {
    val logger = object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Timber.tag("OkHttp")
            Timber.d(message)
        }
    }
    return HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
}