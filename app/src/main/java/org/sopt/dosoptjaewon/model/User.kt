package org.sopt.dosoptjaewon.data.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @DrawableRes
    val profileImage: Int,
    val id: String,
    val pw: String,
    val nickname: String,
    val hobby: String
) : Parcelable