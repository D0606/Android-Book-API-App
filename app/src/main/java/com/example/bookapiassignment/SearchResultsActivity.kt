package com.example.bookapiassignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        val searchTerm : String = intent.getStringExtra("EXTRA_term").toString()
        supportActionBar?.title = "Search Results"
        searchBooks(searchTerm)

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

    private fun searchBooks(searchTerm: String) {
        //Begin processing service call and API results
        val service = ServiceBuilder.buildService(BookService::class.java)
        val requestCall = service.bookSearchRequest(searchTerm)
        requestCall.enqueue(object: Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                Log.d("RESPONSE", response.toString())
                if (response.isSuccessful) {
                    //If the response is good but item count is 0
                    if (response.body()?.totalItems == 0) {
                        Toast.makeText(this@SearchResultsActivity, "No results found!", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else {
                        searchResultDisplay.layoutManager = LinearLayoutManager(this@SearchResultsActivity)
                        searchResultDisplay.adapter = BookListAdapter(response.body()!!)
                    }
                } else {
                    //If error on response
                    AlertDialog.Builder(this@SearchResultsActivity)
                            .setTitle("API error")
                            .setMessage("API Response. Error: ${response.message()}")
                            .setPositiveButton(android.R.string.ok) { _ , _ -> }
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show()
                    Log.d("error1", "API failure with response: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Book>, t: Throwable) {
                //If error without any response
                AlertDialog.Builder(this@SearchResultsActivity)
                        .setTitle("API error")
                        .setMessage("No API Response. Error: $t")
                        .setPositiveButton(android.R.string.ok) { _ , _ -> }
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show()
                Log.d("error2", "API failure with no response: $t")
            }
        })
    }
}