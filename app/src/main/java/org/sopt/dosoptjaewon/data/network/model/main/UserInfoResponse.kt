package org.sopt.dosoptjaewon.data.network.model.main

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponse(
    @SerialName("username") val id: String,
    @SerialName("nickname") val nickname: String
)