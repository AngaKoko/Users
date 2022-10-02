package com.angakoko.vpdmoney.api

import com.angakoko.vpdmoney.model.UserResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * We use a loggin interceptor to catch error from server
 */
private var clientBuilder = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY))


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(clientBuilder.build())
    .baseUrl(BASE_URL)
    .build()

interface UserApiService {
    /**
     * Returns a Coroutine [Deferred] [List] of [UserResponse] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("users")
    fun getUserAsync(): Deferred<MutableList<UserResponse>>

}
/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object UserApi{
    val retrofitService: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }
}