package org.sopt.dosoptjaewon.data.network.repository.main

import org.sopt.dosoptjaewon.data.network.model.main.UserInfoResponse
import org.sopt.dosoptjaewon.data.network.service.AuthService

class MainRepository(private val authService: AuthService) {
    suspend fun getUserInfo(memberId: Int): UserInfoResponse {
        return authService.getUserInfo(memberId)
    }
}