package com.dishcovery.app.di

import com.dishcovery.app.data.datasource.remote.RecipeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecipeNetworkModule {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    fun provideRecipeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}


