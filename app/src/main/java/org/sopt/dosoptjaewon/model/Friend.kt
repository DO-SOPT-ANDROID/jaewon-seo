package org.sopt.dosoptjaewon.model

import androidx.annotation.DrawableRes
import java.util.Date

data class Friend(
    @DrawableRes
    val profileImage: Int,
    val name: String,
    val birthDay: Date,
    val music: String,
    val update: Boolean = false
)
