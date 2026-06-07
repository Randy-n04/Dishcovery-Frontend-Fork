package com.dishcovery.app.di

import com.dishcovery.app.data.datasource.*
import com.dishcovery.app.data.repository.*
import com.dishcovery.app.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(impl: AuthDataSourceImpl): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindChatDataSource(impl: ChatDataSourceImpl): ChatDataSource

    @Binds
    @Singleton
    abstract fun bindProfileDataSource(impl: ProfileDataSourceImpl): ProfileDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds 
    @Singleton
    abstract fun bindImageDataSource(impl: ImageDataSourceImpl): ImageDataSource

    @Binds 
    @Singleton
    abstract fun bindImageRepository(impl: ImageRepositoryImpl): ImageRepository

    @Binds
    @Singleton
    abstract fun bindExchangePlanRepository(impl: ExchangePlanRepositoryImpl): ExchangePlanRepository

    @Binds
    @Singleton
    abstract fun bindMatchRepository(impl: MatchRepositoryImpl): MatchRepository


}
