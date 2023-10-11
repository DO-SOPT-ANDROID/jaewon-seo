package org.sopt.dosopttemplate.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String,
    val pw: String,
    val nickname: String,
    val hobby: String
) : Parcelable