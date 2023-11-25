package org.sopt.dosoptjaewon.data.network.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("username") val id: String,
    @SerialName("password") val password: String
)