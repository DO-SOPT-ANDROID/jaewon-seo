package org.sopt.dosoptjaewon.data.network.repository.signup

import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import org.sopt.dosoptjaewon.data.network.service.SignupService
import retrofit2.Response


class SignupRepository(private val signupService: SignupService) {
    suspend fun signup(request: SignupRequest): Response<Unit> {
        return signupService.signup(request)
    }
}