package com.example.bookapiassignment


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_search.*
import com.google.zxing.integration.android.IntentIntegrator
import android.widget.Toast



class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.title = "Book Search"

        buttonSearch.setOnClickListener {
            //Build search string from generic + author + ISBN if supplied
            val searchTerm = StringBuilder()
            //Attach generic search term to the StringBuilder for val
            searchTerm.append(textSearchTermInput.text)
            //Only attach an author if field is not empty
            if (textAuthorInput.text.toString().isNotEmpty()) {
                searchTerm.append("+inauthor:").append(textAuthorInput.text)
                Log.d("IN AUTHOR", textAuthorInput.text.toString())
            }
            //Only attach an ID search if field is not empty
            if (textISBNInput.text.toString().isNotEmpty()) {
                searchTerm.append("+isbn:").append(textISBNInput.text)
                Log.d("IN ISBN", textISBNInput.text.toString())
            }
            startSearchResultsScreen(searchTerm.toString())
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate search menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Action the menu button press
        return when (item.itemId) {
            R.id.action_scan -> {
                //Barcode scanner settings
                val intentIntegrator = IntentIntegrator(this@SearchActivity)
                intentIntegrator.setBeepEnabled(true)
                intentIntegrator.setCameraId(0)
                intentIntegrator.setPrompt("Scanning Barcode")
                intentIntegrator.setBarcodeImageEnabled(false)
                intentIntegrator.initiateScan()
                Toast.makeText(this, "Scanning ...", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_searchHome -> {
                //Go back to main activity but clear stack
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(homeIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult( requestCode: Int, resultCode: Int, data: Intent? ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val searchTerm = StringBuilder()
        if (result != null) {
            //Begin scan for code
            if (result.contents == null) {
                //If error with scanner
                Toast.makeText(this, "Failed to Acquire Code", Toast.LENGTH_SHORT).show()
            } else {
                //Print out the code in case of partial code read so user can see and search using ISBN search query on API
                Log.d("Scanner", "Scanner succeeded")
                Toast.makeText(this, "Barcode Acquired " + result.contents, Toast.LENGTH_SHORT)
                        .show()
                searchTerm.append("+isbn:").append(result.contents)
                startSearchResultsScreen(searchTerm.toString())
            }
        } else {
            //If issue with barcode scanner
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun startSearchResultsScreen(searchKey:String) {
        if (searchKey.isEmpty()) {
            Toast.makeText(this, "Please enter a search term.", Toast.LENGTH_SHORT).show()
        }
        else{
            //Submit search
            val searchTermIntent = Intent(this, SearchResultsActivity::class.java)
            searchTermIntent.putExtra("EXTRA_term", searchKey)
            startActivity(searchTermIntent)
        }
    }

}
