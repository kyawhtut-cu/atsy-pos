package com.kyawhtut.pos.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Zhenjie Yan on 2018/6/9.
 */
class ServerManager(
    private val activity: AppCompatActivity? = null,
    private val context: Context? = null
) :
    BroadcastReceiver() {
    private val mService: Intent by lazy {
        if (activity != null) Intent(
            activity,
            CoreService::class.java
        ) else Intent(context, CoreService::class.java)
    }

    /**
     * Register broadcast.
     */
    fun register() {
        val filter = IntentFilter(ACTION)
        activity?.registerReceiver(this, filter)
    }

    /**
     * UnRegister broadcast.
     */
    fun unRegister() {
        activity?.unregisterReceiver(this)
    }

    fun startServer() {
        activity?.startService(mService)
    }

    fun stopServer() {
        activity?.stopService(mService)
        context?.stopService(mService)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (ACTION == action) {
            val cmd = intent.getIntExtra(CMD_KEY, 0)
            when (cmd) {
                CMD_VALUE_START -> {
                    val ip = intent.getStringExtra(MESSAGE_KEY)
                    (activity as ServiceInterface).onServerStart(ip)
                }
                CMD_VALUE_ERROR -> {
                    val error = intent.getStringExtra(MESSAGE_KEY)
                    (activity as ServiceInterface).onServerError(error)
                }
                CMD_VALUE_STOP -> {
                    (activity as ServiceInterface).onServerStop()
                }
            }
        }
    }

    companion object {
        private const val ACTION = "com.yanzhenjie.andserver.receiver"
        private const val CMD_KEY = "CMD_KEY"
        private const val MESSAGE_KEY = "MESSAGE_KEY"
        private const val CMD_VALUE_START = 1
        private const val CMD_VALUE_ERROR = 2
        private const val CMD_VALUE_STOP = 4

        /**
         * Notify serverStart.
         *
         * @param context context.
         */
        @JvmStatic
        fun onServerStart(
            context: Context,
            hostAddress: String?
        ) {
            sendBroadcast(
                context,
                CMD_VALUE_START,
                hostAddress
            )
        }

        /**
         * Notify serverStop.
         *
         * @param context context.
         */
        @JvmStatic
        fun onServerError(context: Context, error: String?) {
            sendBroadcast(
                context,
                CMD_VALUE_ERROR,
                error
            )
        }

        /**
         * Notify serverStop.
         *
         * @param context context.
         */
        @JvmStatic
        fun onServerStop(context: Context) {
            sendBroadcast(context, CMD_VALUE_STOP)
        }

        private fun sendBroadcast(
            context: Context,
            cmd: Int,
            message: String? = null
        ) {
            val broadcast = Intent(ACTION)
            broadcast.putExtra(CMD_KEY, cmd)
            broadcast.putExtra(
                MESSAGE_KEY,
                message
            )
            context.sendBroadcast(broadcast)
        }
    }
}
