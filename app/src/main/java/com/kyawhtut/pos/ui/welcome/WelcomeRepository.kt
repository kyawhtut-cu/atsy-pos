package com.kyawhtut.pos.ui.welcome

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
interface WelcomeRepository {

    val isDeviceCheck: Boolean

    var loginPhone: String

    suspend fun checkDevice(phone: String, callback: (WelcomeViewModel.State) -> Unit)
}
