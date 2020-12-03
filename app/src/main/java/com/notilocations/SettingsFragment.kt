package com.notilocations

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SeekBarPreference

/**
 * Shows the user the settings screen.
 */
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Inflates the settings fragment and sets the preferences to the correct values.
     * @param savedInstanceState The previously saved state of the fragment if it exists.
     * @param rootKey The preference key of the preference screen to use as the root of the preference hierarchy
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext())
        findPreference<SeekBarPreference>("max_speed")?.isEnabled =
            sharedPreferences?.getBoolean("max_speed_enabled", false) ?: false

        val defaultDistancePreference: EditTextPreference? = findPreference("distance")

        defaultDistancePreference?.setOnBindEditTextListener { editText ->
            editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }

    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "dark_theme") {
            if (sharedPreferences?.getBoolean(key, false) == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                activity?.recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                activity?.recreate()
            }
        } else if (key == "max_speed_enabled") {
            findPreference<SeekBarPreference>("max_speed")?.isEnabled =
                sharedPreferences?.getBoolean(key, false) ?: false
        }
    }
}






















