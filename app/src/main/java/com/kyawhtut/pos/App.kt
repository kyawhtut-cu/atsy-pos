package com.kyawhtut.pos

import android.app.Application
import android.os.Environment
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.kyawhtut.pos.data.injection.AppInjection
import com.kyawhtut.pos.utils.webserver.net.FileUtils
import com.kyawhtut.sheet2json.Sheet2Json
import com.yanzhenjie.andserver.util.IOUtils
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin
import timber.log.Timber
import java.io.File

class App : Application() {

    companion object {
        var instance: App? = null
    }

    var rootDir: File? = null

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Sheet2Json.init()

        CaocConfig.Builder.create().showErrorDetails(BuildConfig.DEBUG).apply()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidContext(this@App)
            androidFileProperties("rootUser.properties")
            modules(
                listOf(
                    AppInjection.rootUser,
                    AppInjection.preference,
                    AppInjection.network,
                    AppInjection.database,
                    AppInjection.function,
                    AppInjection.authentication,
                    AppInjection.welcome,
                    AppInjection.main,
                    AppInjection.home,
                    AppInjection.category,
                    AppInjection.table,
                    AppInjection.product,
                    AppInjection.customer,
                    AppInjection.ticket
                )
            )
        }

        if (instance == null) {
            instance = this
            if (rootDir != null) return
            rootDir = if (FileUtils.storageAvailable()) {
                Environment.getExternalStorageDirectory()
            } else {
                this.filesDir
            }
            rootDir = File(rootDir, getString(R.string.lbl_pos_web_config))
            IOUtils.createFolder(rootDir)
        }
    }

    private fun initializeDb() {
        Timber.d("initializeDB")
        if (BuildConfig.DEBUG) {
            Timber.d("initializeDB isDebug")
            try {
                val debugDB = Class.forName("com.amitshekhar.DebugDB")
                val getAddressLog = debugDB.getMethod("getAddressLog")
                val value = getAddressLog.invoke(null)
                Timber.d("Open Db -> %s", value as String)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
