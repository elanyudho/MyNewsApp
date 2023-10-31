package com.elanyudho.core.di

import com.elanyudho.core.model.repository.NewsRepository
import com.elanyudho.core.model.usecase.GetArticlesByQueryUseCase
import com.elanyudho.core.model.usecase.GetArticlesUseCase
import com.elanyudho.core.model.usecase.GetSourceByNameUseCase
import com.elanyudho.core.model.usecase.GetSourcesByCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object UseCaseModule {
    @Provides
    @ActivityScoped
    fun provideGetSourcesByCategoryUseCase(repository: NewsRepository) = GetSourcesByCategoryUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideGetSourcesByNameUseCase(repository: NewsRepository) = GetSourceByNameUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideGetArticleUseCase(repository: NewsRepository) = GetArticlesUseCase(repository)

    @Provides
    @ActivityScoped
    fun provideGetArticleByQueryUseCase(repository: NewsRepository) = GetArticlesByQueryUseCase(repository)
}

