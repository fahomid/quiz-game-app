package com.fahomid.quizgame

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Load theme preference before setting the content view
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("night_mode", false)
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Load the HomeFragment initially
            loadFragment(HomeFragment(), "HomeFragment")
        }

        // Handle back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if there are fragments in the back stack
                if (supportFragmentManager.backStackEntryCount > 1) {
                    supportFragmentManager.popBackStack()
                } else {
                    // Show exit confirmation dialog if back stack is empty
                    showExitConfirmationDialog()
                }
            }
        })
    }

    // Load a fragment and ensure it is only added to the back stack once
    private fun loadFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager

        // Check if the fragment is already in the back stack
        val fragmentInBackStack = fragmentManager.findFragmentByTag(tag)
        if (fragmentInBackStack == null) {
            // Add the fragment to the back stack
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit()
        } else {
            // If fragment is already in back stack, pop back stack up to the fragment
            fragmentManager.popBackStackImmediate(tag, 0)
        }
    }

    // Inflate the menu options
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // Handle menu item selections
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home -> {
                // Load HomeFragment when Home menu item is selected
                loadFragment(HomeFragment(), "HomeFragment")
                true
            }
            R.id.settings -> {
                // Load SettingsFragment when Settings menu item is selected
                loadFragment(SettingsFragment(), "SettingsFragment")
                true
            }
            R.id.exit -> {
                // Show exit confirmation dialog when Exit menu item is selected
                showExitConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show a confirmation dialog to exit the app
    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                finish() // This will close the current activity
            }
            .setNegativeButton("No", null)
            .show()
    }
}
