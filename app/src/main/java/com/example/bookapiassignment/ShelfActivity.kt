package com.example.bookapiassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_search_results.*
import kotlinx.android.synthetic.main.activity_shelf.*
import java.io.File
import java.io.InputStream

class ShelfActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shelf)
        supportActionBar?.title = "Book Shelf"
        readShelf()
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

    private fun readShelf() {
        //Retrieve any saved books from file on device storage
        try {
            val inputStream: InputStream = File(filesDir,"savedbooks.txt").inputStream()
            val shelfList: ArrayList<Book.Item> = ArrayList()
            inputStream.bufferedReader().forEachLine {
                //Add book to shelf if found
                val book = Gson().fromJson(it, Book.Item:: class.java)
                shelfList.add(book)
            }
            inputStream.close()
            bookShelfDisplay.layoutManager = GridLayoutManager(this@ShelfActivity, 3)
            bookShelfDisplay.adapter = BookShelfAdapter(shelfList)
        } catch (e: Exception) {
            //If no books found
            e.printStackTrace()
            Toast.makeText(this, "Empty shelf!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRestart() {
        //Refresh activity
        super.onRestart()
        recreate()
    }
}