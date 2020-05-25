package com.kyawhtut.pos.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kyawhtut.pos.service.ServerManager
import com.kyawhtut.pos.utils.Constants

/**
 * @author kyawhtut
 * @date 04/05/2020
 */
class StopNotificationIntent : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ServerManager(context = context).stopServer()
        NotificationManagerUtil.Builder(context)
            .build(Constants.SERVER_CHANNEL_ID, Constants.SERVER_CHANNEL_NAME)
            .cancel(Constants.SERVER_NOTIFICATION_ID)
    }
}
