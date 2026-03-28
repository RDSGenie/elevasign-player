package com.elevasign.player.data.remote

import com.elevasign.player.data.remote.dto.CommandResultRequest
import com.elevasign.player.data.remote.dto.HeartbeatRequest
import com.elevasign.player.data.remote.dto.HeartbeatResponse
import com.elevasign.player.data.remote.dto.RegisterRequest
import com.elevasign.player.data.remote.dto.RegisterResponse
import com.elevasign.player.data.remote.dto.SyncResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApi {

    @POST("player-register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("player-sync")
    suspend fun sync(@Query("screen_id") screenId: String): SyncResponse

    @POST("player-heartbeat")
    suspend fun heartbeat(@Body request: HeartbeatRequest): HeartbeatResponse

    @POST("player-command-result")
    suspend fun commandResult(@Body request: CommandResultRequest)
}
