package com.kyawhtut.pos.ui.setting

import android.app.PendingIntent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.commit
import com.kyawhtut.pos.R
import com.kyawhtut.pos.service.ServerManager
import com.kyawhtut.pos.service.ServiceInterface
import com.kyawhtut.pos.utils.Constants
import com.kyawhtut.pos.utils.getColorValue
import com.kyawhtut.pos.utils.intentFor
import com.kyawhtut.pos.utils.notification.NotificationManagerUtil
import com.kyawhtut.pos.utils.notification.StopNotificationIntent
import com.yanzhenjie.loading.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_setting.*
import timber.log.Timber

class SettingActivity : AppCompatActivity(), ServiceInterface {

    private val serverManager: ServerManager by lazy {
        ServerManager(this).apply {
            register()
        }
    }
    private val loading: LoadingDialog by lazy { LoadingDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null)
            supportFragmentManager.commit {
                add(R.id.frame_layout, SettingFragment())
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun startServer() {
        loading.show()
        serverManager.startServer()
    }

    override fun stopServer() {
        loading.show()
        serverManager.stopServer()
    }

    override fun onServerStart(serverIP: String?) {
        serverIP?.let {
            if (loading.isShowing) loading.dismiss()
            (supportFragmentManager.findFragmentById(R.id.frame_layout) as SettingFragment).serverStatus()
            showNotification(it)
            if (it.isNotEmpty()) {
                Timber.d("onServerStart Server Started => http://%s:8080/", serverIP)
                Timber.d("onServerStart DB Started => http://%s:8081/", serverIP)
            } else {
                Timber.d("IP Address not found.")
                onServerStop()
            }
        }
    }

    override fun onServerStop() {
        if (loading.isShowing) loading.dismiss()
        Timber.d("onServer Stop => Server stop success")
        showNotification(null)
        (supportFragmentManager.findFragmentById(R.id.frame_layout) as SettingFragment).serverStatus()
    }

    override fun onServerError(error: String?) {
        Timber.e(error)
        onServerStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serverManager.unRegister()
    }

    private fun showNotification(serverIP: String? = null) {
        val manager = NotificationManagerUtil.Builder(this).apply {
            channelId = Constants.SERVER_CHANNEL_ID
            channelName = Constants.SERVER_CHANNEL_NAME
        }.build()
        if (serverIP == null) {
            manager.cancel(Constants.SERVER_NOTIFICATION_ID)
            return
        }
        val notification = NotificationCompat.Builder(this, Constants.SERVER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_server)
            .setContentTitle("Running server")
            .setContentText("Server IP Address - http://$serverIP:8080/")
            .setAutoCancel(false)
            .setOngoing(true)
            .setWhen(System.currentTimeMillis())
            .setColor(getColorValue(R.color.colorPrimary))

        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intentFor<StopNotificationIntent>(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notification.addAction(R.drawable.ic_calendar_white, "Stop", stopPendingIntent)
        manager.notify(Constants.SERVER_NOTIFICATION_ID, notification.build())
    }
}
