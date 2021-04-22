package com.example.bookapiassignment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*


lateinit var sharedPreferences: SharedPreferences
val modeKey = "darkMode"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
            "modePreference",
            Context.MODE_PRIVATE
        )
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "BookKeeper"
        buttonSearchScreen.setOnClickListener { startSearchScreen() }
        buttonShelfScreen.setOnClickListener { startShelfScreen() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate main menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Action the menu button press
        return when (item.itemId) {
            R.id.action_settings -> {
                //Ensure switch status is passed
                var darkModeKey = false
                when (sharedPreferences.getString(modeKey, "lightMode")) {
                    "lightMode" -> {
                        darkModeKey = false
                    }
                    "darkMode" -> {
                        darkModeKey = true
                    }

                }
                startSettingsScreen(darkModeKey)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startSearchScreen() {
        val searchIntent = Intent(this, SearchActivity::class.java)
        startActivity(searchIntent)
    }

    private fun startShelfScreen() {
        val shelfIntent = Intent(this, ShelfActivity::class.java)
        startActivity(shelfIntent)
    }

    private fun startSettingsScreen(darkModeKey:Boolean) {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsIntent.putExtra("isLightMode", darkModeKey)
        startActivity(settingsIntent)
    }

}
