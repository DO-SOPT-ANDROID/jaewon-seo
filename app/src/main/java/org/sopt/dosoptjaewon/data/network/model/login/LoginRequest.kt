package org.sopt.dosoptjaewon.data.network.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val id: String,
    val password: String
)