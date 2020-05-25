package com.kyawhtut.pos.data.api.network

import com.kyawhtut.pos.data.api.response.DeviceStatusResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
interface API {

    @FormUrlEncoded
    @POST("/api/pos.php")
    suspend fun getDeviceStatus(
        @Field("action") action: String,
        @Field("phone") phone: String
    ): DeviceStatusResponse

}