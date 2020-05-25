package com.kyawhtut.pos.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.kyawhtut.pos.service.ServerManager.Companion.onServerError
import com.kyawhtut.pos.service.ServerManager.Companion.onServerStart
import com.kyawhtut.pos.service.ServerManager.Companion.onServerStop
import com.kyawhtut.pos.utils.webserver.net.NetUtils.localIPAddress
import com.yanzhenjie.andserver.AndServer
import com.yanzhenjie.andserver.Server
import com.yanzhenjie.andserver.Server.ServerListener
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CoreService : Service() {

    companion object {
        var mServer: Server? = null
    }

    override fun onCreate() {
        super.onCreate()
        if (mServer == null) {
            mServer = AndServer
                .serverBuilder(this)
                .inetAddress(localIPAddress)
                .port(8080)
                .timeout(10, TimeUnit.SECONDS)
                .listener(object : ServerListener {
                    override fun onStarted() {
                        val hostAddress = mServer!!.inetAddress.hostAddress
                        onServerStart(
                            this@CoreService,
                            hostAddress
                        )
                    }

                    override fun onStopped() {
                        onServerStop(this@CoreService)
                    }

                    override fun onException(e: Exception) {
                        Timber.e(e, "onException")
                        onServerError(
                            this@CoreService,
                            e.message
                        )
                    }
                })
                .build()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startServer()
        return START_STICKY
    }

    override fun onDestroy() {
        stopServer()
        super.onDestroy()
    }

    /**
     * Start server.
     */
    private fun startServer() {
        if (mServer!!.isRunning) {
            mServer!!.shutdown()
        } else {
            mServer!!.startup()
        }
    }

    /**
     * Stop server.
     */
    private fun stopServer() {
        mServer!!.shutdown()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
