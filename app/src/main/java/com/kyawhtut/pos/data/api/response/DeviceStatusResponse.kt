package com.kyawhtut.pos.data.api.response

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
@Keep
data class DeviceStatusResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)

fun String.toDeviceStatusResponse(): DeviceStatusResponse =
    Gson().fromJson<DeviceStatusResponse>(this, object : TypeToken<DeviceStatusResponse>() {}.type)
