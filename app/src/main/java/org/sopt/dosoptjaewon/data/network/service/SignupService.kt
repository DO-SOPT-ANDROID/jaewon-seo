package org.sopt.dosoptjaewon.data.network.service

import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("api/v1/members")
    suspend fun signup(
        @Body request: SignupRequest
    ): Response<Unit>
}