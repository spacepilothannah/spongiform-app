package com.github.spacepilothannah.spongiform.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.http.*
import com.github.spacepilothannah.spongiform.data.api.dto.*
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.github.spacepilothannah.spongiform.util.RFC8601JsonAdapter

class SpongiformService {
    interface Api {
        @GET("requests")
    fun getRequests(@Query("pending") pending : Boolean?,
                    @Query("allowed") allowed : Boolean?): Call<List<Request>>

        @GET("requests/{id}")
        fun getRequest(@Path("id") id : Int): Call<Request>

        @PUT("requests/{id}")
        fun updateRequest(@Path("id") id : Int, @Body update: RequestUpdate) : Call<Request>
    }

    companion object {
        private fun makeOkHttpClient(username: String, password: String): OkHttpClient {
            return OkHttpClient().newBuilder().addInterceptor { chain ->
                val originalRequest = chain.request()

                    val builder = originalRequest.newBuilder().header("Authorization",
                            Credentials.basic(username, password))

                    val newRequest = builder.build()
                    chain.proceed(newRequest)
                }.build()
        }
        fun getApiClient(url: String, username: String, password: String): Api {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(RFC8601JsonAdapter())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))

                    .client(makeOkHttpClient(username, password))
                    .build()



            return retrofit.create(Api::class.java)
        }
    }
}

