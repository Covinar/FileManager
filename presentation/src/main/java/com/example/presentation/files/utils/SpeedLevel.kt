package com.example.presentation.files.utils

import androidx.annotation.DrawableRes
import com.example.presentation.R

enum class SpeedLevel(@DrawableRes val levelId: Int, val  level: Float) {

    X075(R.string.speed_075, 0.75F),
    X1(R.string.speed_1, 1F),
    X125(R.string.speed_125, 1.25F),
    X15(R.string.speed_15, 1.5F),
    X175(R.string.speed_175, 1.75F),
    X2(R.string.speed_2, 2F)

}