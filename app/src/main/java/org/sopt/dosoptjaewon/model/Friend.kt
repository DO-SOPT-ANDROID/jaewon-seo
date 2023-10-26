package org.sopt.dosoptjaewon.model

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class Friend(
    @DrawableRes
    val profileImage: Int,
    val name: String,
    val birthDay: LocalDate,
    val music: String = "",
    val update: Boolean = false
)