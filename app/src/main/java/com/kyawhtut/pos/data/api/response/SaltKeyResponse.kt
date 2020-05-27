package com.kyawhtut.pos.data.api.response

import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

/**
 * @author kyawhtut
 * @date 27/05/2020
 */
@Keep
data class SaltKeyResponse(
    @SerializedName("key")
    val key: String
)

fun String.toSaltKeyResponse(): SaltKeyResponse = Gson().fromJson<List<SaltKeyResponse>>(
    this,
    object : TypeToken<List<SaltKeyResponse>>() {}.type
).first()
