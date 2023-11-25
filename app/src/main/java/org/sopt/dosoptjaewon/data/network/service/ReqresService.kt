package org.sopt.dosoptjaewon.data.network.service

import org.sopt.dosoptjaewon.data.network.model.main.reqres.ReqresResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ReqresService {
    @GET("/api/users")
    suspend fun getFollowerList(
        @Query("page") num: Int = 2
    ): ReqresResponse
}