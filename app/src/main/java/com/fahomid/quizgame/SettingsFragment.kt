package com.fahomid.quizgame

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat

class SettingsFragment : Fragment() {

    // UI component for the theme switch
    private lateinit var themeSwitch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize the theme switch
        themeSwitch = view.findViewById(R.id.theme_switch)

        // Load the saved theme preference
        val sharedPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("night_mode", false)
        themeSwitch.isChecked = isNightMode

        // Set listener for switch changes
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("night_mode", isChecked)
            editor.apply()

            // Apply the theme
            applyTheme(isChecked)
        }

        return view
    }

    // Apply the selected theme and restart the activity to apply changes
    private fun applyTheme(isNightMode: Boolean) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        // Restart the activity to apply the theme change
        requireActivity().recreate()
    }
}
