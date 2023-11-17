package org.sopt.dosoptjaewon.data.network.model.signup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    @SerialName("username")
    val id: String = "",
    @SerialName("password")
    val password: String = "",
    @SerialName("nickname")
    val nickname: String = ""
)