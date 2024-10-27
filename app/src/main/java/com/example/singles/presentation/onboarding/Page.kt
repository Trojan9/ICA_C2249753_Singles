package com.example.singles.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.singles.R

data class Page(
    val description:String,
    @DrawableRes val image: Int,
)

val pages = listOf(
    Page(

        description = "Hot singles looking to link up with their taste, join now to meet up with various people.",
        image = R.drawable.onboarding1
    ),
    Page(

        description = "Get ready to skip the talking stage and have fun with satisfaction , we do the talking for you.",
        image = R.drawable.onboarding2
    ),
    Page(

        description = "Get ready to skip the talking stage and have fun with satisfaction , we do the talking for you.",
        image = R.drawable.onboarding3
    )
)
