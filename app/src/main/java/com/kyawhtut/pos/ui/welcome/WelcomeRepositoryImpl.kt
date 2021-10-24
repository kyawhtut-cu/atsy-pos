package com.kyawhtut.pos.ui.welcome

import android.content.SharedPreferences
import com.kyawhtut.pos.BuildConfig
import com.kyawhtut.pos.data.api.network.API
import com.kyawhtut.pos.data.api.response.toDeviceStatusResponse
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.data.sharedpreference.put
import com.kyawhtut.pos.utils.SHKey
import tgio.rncryptor.RNCryptorNative

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
class WelcomeRepositoryImpl(private val sh: SharedPreferences, private val api: API) :
    WelcomeRepository {

    override val isDeviceCheck: Boolean
        get() = loginPhone.isNotEmpty()

    override var loginPhone: String
        get() = sh.get(SHKey.KEY_PHONE_NUMBER, "")
        set(value) = sh.put(SHKey.KEY_PHONE_NUMBER, value)

    override suspend fun checkDevice(phone: String, callback: (WelcomeViewModel.State) -> Unit) {
        try {
            callback(WelcomeViewModel.State.LOADING)
            val response = api.getDeviceStatus("deviceCheck", phone)

            val checkDeviceResponse = RNCryptorNative().decrypt(
                response.value, BuildConfig.ENCRYPTION_KEY
            ).toDeviceStatusResponse()
            if (checkDeviceResponse.status == 200) {
                loginPhone = phone
                callback(
                    WelcomeViewModel.State.FINISH(
                        checkDeviceResponse.status,
                        checkDeviceResponse.message
                    )
                )
            } else if (checkDeviceResponse.status == 204 && isDeviceCheck) {
                callback(
                    WelcomeViewModel.State.FINISH(
                        checkDeviceResponse.status,
                        checkDeviceResponse.message
                    )
                )
            } else {
                if (checkDeviceResponse.status == 405 && isDeviceCheck) loginPhone = ""
                callback(
                    WelcomeViewModel.State.ERROR(
                        checkDeviceResponse.status,
                        checkDeviceResponse.message
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(
                WelcomeViewModel.State.ERROR(
                    500,
                    e.localizedMessage ?: "Something went wrong."
                )
            )
        }
    }
}
