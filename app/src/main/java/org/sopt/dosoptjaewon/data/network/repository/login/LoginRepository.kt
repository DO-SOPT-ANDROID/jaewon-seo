package org.sopt.dosoptjaewon.data.network.repository.login

import org.sopt.dosoptjaewon.data.network.model.login.LoginRequest
import org.sopt.dosoptjaewon.data.network.model.login.LoginResponse
import org.sopt.dosoptjaewon.data.network.service.AuthService

class LoginRepository(private val authService: AuthService) {
    suspend fun login(request: LoginRequest): LoginResponse {
        return authService.login(request)
    }
}
