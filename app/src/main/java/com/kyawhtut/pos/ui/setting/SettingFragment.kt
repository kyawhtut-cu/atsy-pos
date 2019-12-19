package com.kyawhtut.pos.ui.setting

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kyawhtut.pos.R
import java.util.regex.Pattern

class SettingFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private var prefsVersion: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)

        prefsVersion = findPreference("prefs_version_name")
        setVersion()
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        return true
    }

    private fun setVersion() {
        try {
            val packageInfo = context?.packageManager?.getPackageInfo(context?.packageName ?: "", 0)
            val versionName = packageInfo?.versionName ?: ""
            val regx = "^([\\\\d]+).([\\\\d]+).([\\\\d]+).([\\\\d]{4,})([\\\\d]{2,})([\\\\d]{2,})\$"
            val pattern = Pattern.compile(regx)
            val matcher = pattern.matcher(versionName)
            if (matcher.matches()) {
                val summary =
                    String.format(
                        "%s.%s.%s(Date: %s/%s/%s",
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        matcher.group(6),
                        matcher.group(5),
                        matcher.group(4)
                    )
                prefsVersion?.summary = summary
            } else {
                prefsVersion?.summary = versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}