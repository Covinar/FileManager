package com.example.domain.usecases

interface GetExternalStorageDirUseCase {

    operator fun invoke() : List<String>

}