package com.kyawhtut.pos

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.kyawhtut.pos.data.injection.AppInjection
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        CaocConfig.Builder.create().showErrorDetails(BuildConfig.DEBUG).apply()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidContext(this@App)
            androidFileProperties("rootUser.properties")
            modules(
                listOf(
                    AppInjection.rootUser,
                    AppInjection.preference,
                    AppInjection.database,
                    AppInjection.function,
                    AppInjection.authentication,
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

        initializeDb()
    }

    private fun initializeDb() {
        if (BuildConfig.DEBUG) {
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