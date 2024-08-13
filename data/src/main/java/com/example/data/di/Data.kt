package com.example.data.di

import android.content.Context
import com.example.data.repositories.FilesRepositoryImpl
import com.example.domain.repositories.FilesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Data {

    @Provides
    fun provideFilesRepository(@ApplicationContext context: Context): FilesRepository = FilesRepositoryImpl(context)

}