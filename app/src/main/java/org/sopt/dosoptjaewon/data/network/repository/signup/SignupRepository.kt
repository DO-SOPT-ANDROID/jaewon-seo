package org.sopt.dosoptjaewon.data.network.repository.signup

import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import org.sopt.dosoptjaewon.data.network.service.AuthService
import retrofit2.Response


class SignupRepository(private val authService: AuthService) {
    suspend fun signup(request: SignupRequest): Response<Unit> {
        return authService.signup(request)
    }
}