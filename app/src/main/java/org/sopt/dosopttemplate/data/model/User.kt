package org.sopt.dosopttemplate.data.model

import java.io.Serializable

data class User(
    val id: String,
    val pw: String,
    val nickname: String,
    val hobby: String
) : Serializable