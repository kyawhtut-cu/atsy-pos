package com.kyawhtut.pos.data.api.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * @author kyawhtut
 * @date 27/05/2020
 */
@Keep
data class ApiResponse(
    @SerializedName("value")
    val value: String
)
