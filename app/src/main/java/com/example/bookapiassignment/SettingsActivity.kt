package com.example.bookapiassignment

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var nightMode = false
        val uiMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        sharedPreferences = getSharedPreferences(
                "modePreference",
                Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"

        //Check user settings for dark mode and set preferences and switch to match
        if (uiMode == Configuration.UI_MODE_NIGHT_NO) {
            sharedPreferences.edit().putString(modeKey, "lightMode").apply()
            nightMode = false
        }
        if (uiMode == Configuration.UI_MODE_NIGHT_YES) {
           sharedPreferences.edit().putString(modeKey, "darkMode").apply()
           nightMode = true
        }
        nightSwitch.isChecked = nightMode
        nightSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferences.edit().putString(modeKey, "darkMode").apply()
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferences.edit().putString(modeKey, "lightMode").apply()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate basic menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_basic, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Action the home button press
        return when (item.itemId) {
            R.id.action_basicHome -> {
                //Go back to main activity but clear stack
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(homeIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}