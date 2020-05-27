package com.kyawhtut.pos.ui.setting

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.kyawhtut.fontchooserlib.FontChoose
import com.kyawhtut.pos.BuildConfig
import com.kyawhtut.pos.R
import com.kyawhtut.pos.data.sharedpreference.get
import com.kyawhtut.pos.data.sharedpreference.put
import com.kyawhtut.pos.service.CoreService
import com.kyawhtut.pos.utils.Constants
import com.kyawhtut.pos.utils.getInflateView
import com.kyawhtut.pos.utils.openFacebookAccount
import com.kyawhtut.pos.utils.showDialog
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.dialog_change_limit.view.*
import kotlinx.android.synthetic.main.dialog_change_percentage.view.*
import kotlinx.android.synthetic.main.dialog_print_header_footer_editor.view.*
import java.util.regex.Pattern

class SettingFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    private var prefsVersion: Preference? = null
    private var prefsPrintHeader: Preference? = null
    private var prefsPrintFooter: Preference? = null
    private var prefsRunAsServer: Preference? = null
    private var prefsFontChange: Preference? = null
    private var prefsDeveloper: Preference? = null

    private var prefsTax: Preference? = null
    private var prefsLimitAmount: Preference? = null
    private var taxAmount: Int = 0
    private var limitAmount: Int = 0
    private val isTrial = BuildConfig.isTrial

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.setting)

        taxAmount = preferenceManager.sharedPreferences.get(Constants.KEY_TAX_AMOUNT, 5)
        limitAmount = preferenceManager.sharedPreferences.get(Constants.KEY_LIMIT_AMOUNT, 5)

        prefsVersion = findPreference("prefs_version_name")
        prefsPrintHeader = findPreference("pref_print_header_key")
        prefsPrintFooter = findPreference("pref_print_footer_key")
        prefsRunAsServer = findPreference("pref_run_as_server")
        prefsTax = findPreference("pref_tax_key")
        prefsLimitAmount = findPreference("pref_alert_amount_key")
        prefsFontChange = findPreference("pref_font_key")
        prefsDeveloper = findPreference("prefs_developed_by")

        setVersion()

        prefsPrintHeader?.onPreferenceClickListener = this
        prefsPrintFooter?.onPreferenceClickListener = this
        prefsRunAsServer?.onPreferenceClickListener = this
        prefsTax?.onPreferenceClickListener = this
        prefsLimitAmount?.onPreferenceClickListener = this
        prefsFontChange?.onPreferenceClickListener = this
        prefsDeveloper?.onPreferenceClickListener = this

        bindTaxAmount()
        bindLimitAmount()

        serverStatus()
    }

    private fun bindTaxAmount() {
        prefsTax?.let {
            it.summary = "%s %s".format(taxAmount, "%")
        }
    }

    private fun bindLimitAmount() {
        prefsLimitAmount?.let {
            it.summary = "$limitAmount"
        }
    }

    fun serverStatus() {
        prefsRunAsServer?.summary = if (CoreService.mServer?.isRunning == true) {
            "Running at - ${CoreService.mServer?.inetAddress?.hostAddress}"
        } else {
            "Server stop."
        }
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        when (preference) {
            prefsDeveloper -> openFacebookAccount("100008526678537")
            prefsPrintHeader -> {
                if (isTrial) {
                    Toasty.warning(
                        requireContext(),
                        "အစမ်း Version တွင် မရနိုင်သေးပါ။",
                        Toasty.LENGTH_LONG
                    ).show()
                } else
                    showEditor("Edit Print Header Format", 0)
            }
            prefsPrintFooter -> {
                if (isTrial) {
                    Toasty.warning(
                        requireContext(),
                        "အစမ်း Version တွင် မရနိုင်သေးပါ။",
                        Toasty.LENGTH_LONG
                    ).show()
                } else
                    showEditor("Edit Print Footer Format", 1)
            }
            prefsRunAsServer -> {
                if (isTrial) {
                    Toasty.warning(
                        requireContext(),
                        "အစမ်း Version တွင် မရနိုင်သေးပါ။",
                        Toasty.LENGTH_LONG
                    ).show()
                } else
                    (requireActivity() as SettingActivity).startServer()
                        .takeUnless { CoreService.mServer?.isRunning ?: false }
                        ?: (requireActivity() as SettingActivity).stopServer()
            }
            prefsFontChange -> {
                FontChoose.change(requireActivity() as SettingActivity)
            }
            prefsLimitAmount -> {
                requireContext().showDialog(
                    requireContext().getInflateView(R.layout.dialog_change_limit),
                    bindView = {
                        this.edt_limit.apply {
                            setText("$limitAmount")
                            addTextChangedListener {
                                limitAmount = (this.text?.toString()
                                    .takeIf { it != "0" && !it.isNullOrEmpty() } ?: "0").toInt()
                            }
                        }
                    },
                    onClickPositive = {
                        text = "Ok"
                        onClick = {
                            preferenceManager.sharedPreferences.put(
                                Constants.KEY_LIMIT_AMOUNT,
                                limitAmount
                            )
                            bindLimitAmount()
                            it.dismiss()
                        }
                    },
                    onClickNegative = {
                        text = "Cancel"
                        onClick = {
                            it.dismiss()
                        }
                    }
                )
            }
            prefsTax -> {
                requireContext().showDialog(
                    requireContext().getInflateView(R.layout.dialog_change_percentage),
                    bindView = {
                        this.edt_percentage.apply {
                            setText("$taxAmount")
                            addTextChangedListener {
                                taxAmount = (this.text?.toString()
                                    .takeIf { it != "0" && !it.isNullOrEmpty() } ?: "0").toInt()
                            }
                        }
                    },
                    onClickPositive = {
                        text = "Ok"
                        onClick = {
                            preferenceManager.sharedPreferences.put(
                                Constants.KEY_TAX_AMOUNT,
                                taxAmount
                            )
                            bindTaxAmount()
                            it.dismiss()
                        }
                    },
                    onClickNegative = {
                        text = "Cancel"
                        onClick = {
                            it.dismiss()
                        }
                    }
                )
            }
        }
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

    private fun showEditor(title: String, type: Int) {
        requireContext().showDialog(
            requireContext().getInflateView(R.layout.dialog_print_header_footer_editor),
            bindView = { dialog ->
                this.tv_editor_title.text = title
                this.editor_view.html = preferenceManager.sharedPreferences.get(
                    when (type) {
                        0 -> Constants.KEY_PRINT_HEADER
                        else -> Constants.KEY_PRINT_FOOTER
                    },
                    when (type) {
                        0 -> Constants.DEFAULT_HEADER
                        else -> Constants.DEFAULT_FOOTER
                    }
                )
                this.editor_tool_bar.apply {
                    textEditorView = this@showDialog.editor_view
                    onClick = {

                    }
                }
                this.btn_cancel.setOnClickListener {
                    dialog.dismiss()
                }
                this.btn_confirm.setOnClickListener {
                    preferenceManager.sharedPreferences.put(
                        when (type) {
                            0 -> Constants.KEY_PRINT_HEADER
                            else -> Constants.KEY_PRINT_FOOTER
                        },
                        this.editor_view.html
                    )
                    dialog.dismiss()
                }
            },
            isCancelable = false,
            isTransparent = true
        )
    }
}
