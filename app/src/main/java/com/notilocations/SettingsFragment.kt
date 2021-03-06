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
     * Inflates the settings fragment and sets up the preference views.
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

    /**
     * Registers this as an on shared preference change listener when the settings fragment is resumed.
     */
    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    /**
     * Unregisters this as an on shared preference change listener when the settings fragment is paused.
     */
    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    /**
     * Called when a shared preference changes. If the changed preference is the dark_theme preference, sets the app to dark theme. If
     * it is max_speed_enabled, sets the max speed input to the appropriate value.
     * @param sharedPreferences The shared preference object that was changed.
     * @param key The key of the preference that was changed.
     */
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






















