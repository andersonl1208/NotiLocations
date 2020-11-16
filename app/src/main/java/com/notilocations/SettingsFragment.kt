package com.notilocations

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

/**
 * Shows the user the settings screen.
 */
class SettingsFragment : PreferenceFragmentCompat() {

    /**
     * Inflates the settings fragment and sets the preferences to the correct values.
     * @param savedInstanceState The previously saved state of the fragment if it exists.
     * @param rootKey The preference key of the preference screen to use as the root of the preference hierarchy
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}