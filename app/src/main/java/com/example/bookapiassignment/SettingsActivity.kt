package com.example.bookapiassignment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
                "modePreference",
                Context.MODE_PRIVATE
        )

        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"
        val nightMode = intent.getBooleanExtra("isLightMode", false)
        nightSwitch.isChecked = nightMode != false
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