package com.dishcovery.app.di

import com.dishcovery.app.data.datasource.remote.ChatApiService
import com.dishcovery.app.data.datasource.remote.ExchangePlanApiService
import com.dishcovery.app.data.datasource.remote.FaqApiService
import com.dishcovery.app.data.datasource.remote.ImageApiService
import com.dishcovery.app.data.datasource.remote.KitchenCategoryApiService
import com.dishcovery.app.data.datasource.remote.MatchApiService
import com.dishcovery.app.data.datasource.remote.ProfileApiService
import com.dishcovery.app.data.datasource.remote.RecipeApiService
import com.dishcovery.app.data.datasource.remote.UserApiService
import com.dishcovery.app.data.remote.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // 10.0.2.2 para emulador, localhost para pruebas locales de Java
    private const val BASE_URL = "http://10.0.2.2:8080/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideChatApiService(retrofit: Retrofit): ChatApiService =
        retrofit.create(ChatApiService::class.java)

    @Provides
    @Singleton
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideKitchenCategoryApiService(retrofit: Retrofit): KitchenCategoryApiService =
        retrofit.create(KitchenCategoryApiService::class.java)

    @Provides
    @Singleton
    fun provideFoodAnalysisApiService(retrofit: Retrofit): ImageApiService =
        retrofit.create(ImageApiService::class.java)

    @Provides
    @Singleton
    fun provideExchangePlanApiService(retrofit: Retrofit): ExchangePlanApiService =
        retrofit.create(ExchangePlanApiService::class.java)

    @Provides
    @Singleton
    fun provideMatchApiService(retrofit: Retrofit): MatchApiService =
        retrofit.create(MatchApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideFaqApiService(retrofit: Retrofit): FaqApiService =
        retrofit.create(FaqApiService::class.java)
}