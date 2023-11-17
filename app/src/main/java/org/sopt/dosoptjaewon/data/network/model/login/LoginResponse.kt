package org.sopt.dosoptjaewon.data.network.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("id") val memberId: Int = 0,
    @SerialName("username") val id: String = "",
    @SerialName("nickname") val nickname: String = ""
)