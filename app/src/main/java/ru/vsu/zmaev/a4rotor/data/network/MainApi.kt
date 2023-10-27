package ru.vsu.zmaev.a4rotor.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object MainApi {
    private var mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private var mOkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(mHttpLoggingInterceptor)
        .build()

    private var mRetrofit: Retrofit? = null

    const val URL = ""

    val client: Retrofit?
        get() {
            if(mRetrofit == null){
                mRetrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(mOkHttpClient)
                    .build()
            }
            return mRetrofit
        }
}
