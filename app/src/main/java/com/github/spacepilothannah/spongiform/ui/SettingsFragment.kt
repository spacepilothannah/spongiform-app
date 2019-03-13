package com.github.spacepilothannah.spongiform.ui

import android.os.Bundle
import androidx.preference.PreferenceFragment
import com.github.spacepilothannah.spongiform.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}