package org.sopt.dosoptjaewon.data.network.service

import org.sopt.dosoptjaewon.data.network.model.login.LoginRequest
import org.sopt.dosoptjaewon.data.network.model.login.LoginResponse
import org.sopt.dosoptjaewon.data.network.model.main.UserInfoResponse
import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthService {
    @POST("api/v1/members")
    suspend fun postSignup(
        @Body request: SignupRequest
    ): Response<Unit>

    @POST("api/v1/members/sign-in")
    suspend fun postLogin(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("/api/v1/members/{memberId}")
    suspend fun getUserInfo(
        @Path("memberId") memberId: Int
    ): UserInfoResponse
}