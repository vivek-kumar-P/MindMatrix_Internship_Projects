package com.example.thirtydaystravel.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Destination(
    val day: Int,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
