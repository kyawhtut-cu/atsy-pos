package com.kyawhtut.pos.data.injection

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceInjector {

    fun provideSharedPreference(context: Context, name: String? = null): SharedPreferences {
        return if (name == null) PreferenceManager.getDefaultSharedPreferences(context)
        else context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }
}