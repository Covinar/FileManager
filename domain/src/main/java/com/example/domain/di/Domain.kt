package com.example.domain.di

import com.example.domain.repositories.FilesRepository
import com.example.domain.usecases.CreateNewFolderUseCase
import com.example.domain.usecases.CreateNewFolderUseCaseImpl
import com.example.domain.usecases.DeleteFileUseCase
import com.example.domain.usecases.DeleteFileUseCaseImpl
import com.example.domain.usecases.FilterFilesUseCase
import com.example.domain.usecases.FilterFilesUseCaseImpl
import com.example.domain.usecases.GetExternalStorageDirUseCase
import com.example.domain.usecases.GetExternalStorageDirUseCaseImpl
import com.example.domain.usecases.GetFileUseCase
import com.example.domain.usecases.GetFileUseCaseImpl
import com.example.domain.usecases.GetFilesUseCase
import com.example.domain.usecases.GetFilesUseCaseImpl
import com.example.domain.usecases.SearchFilesUseCase
import com.example.domain.usecases.SearchFilesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object Domain {

    @Provides
    fun provideGetFilesUseCase(filesRepository: FilesRepository): GetFilesUseCase = GetFilesUseCaseImpl(filesRepository)

    @Provides
    fun provideSearchFilesUseCase(filesRepository: FilesRepository): SearchFilesUseCase = SearchFilesUseCaseImpl(filesRepository)

    @Provides
    fun provideGetExternalStorageDirectories(filesRepository: FilesRepository): GetExternalStorageDirUseCase = GetExternalStorageDirUseCaseImpl(filesRepository)

    @Provides
    fun provideGetFileUseCase(filesRepository: FilesRepository): GetFileUseCase = GetFileUseCaseImpl(filesRepository)

    @Provides
    fun provideFilterFilesUseCase(filesRepository: FilesRepository): FilterFilesUseCase = FilterFilesUseCaseImpl(filesRepository)

    @Provides
    fun provideDeleteFileUseCase(filesRepository: FilesRepository): DeleteFileUseCase = DeleteFileUseCaseImpl(filesRepository)

    @Provides
    fun provideCreateNewFolderUseCase(filesRepository: FilesRepository): CreateNewFolderUseCase = CreateNewFolderUseCaseImpl(filesRepository)

}