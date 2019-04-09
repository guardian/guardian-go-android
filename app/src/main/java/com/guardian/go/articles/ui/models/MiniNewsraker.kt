package com.guardian.go.articles.ui.models

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


interface MiniNewsraker {

    @GET
    fun getGroup(@Url uri: String): Single<Group>

    @GET
    fun getFront(@Url uriWithParams: String): Single<Front>

    @GET
    fun getArticleItem(@Url uri: String): Single<ArticleItem>

    @GET
    fun doGet(@Url uri: String): Single<Response<Any>>

    companion object {
        private var instance: MiniNewsraker? = null

        private fun create(context: Context) : MiniNewsraker {
            val cacheSize = 100 * 1024 * 1024L // 100 MB
            val cache = Cache(context.cacheDir, cacheSize)

            val okHttpClient = OkHttpClient.Builder()
                .cache(cache)
                .build()

            val objectMapper = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val retrofit = Retrofit.Builder()
                .baseUrl("https://ignored/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()

            return retrofit.create(MiniNewsraker::class.java)
        }

        fun get(context: Context) = instance ?: create(context).also { instance = it }
    }
}
