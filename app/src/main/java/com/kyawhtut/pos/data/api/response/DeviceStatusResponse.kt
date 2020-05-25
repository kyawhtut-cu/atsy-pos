package com.kyawhtut.pos.data.api.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

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
