package com.example.bookapiassignment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_info.*
import kotlinx.android.synthetic.main.book_search_results.*
import java.io.*


class BookInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_info)
        val selectedBookInfo = intent.getStringExtra("EXTRA_bookInfo")
        val selectedBook = Gson().fromJson(selectedBookInfo, Book.Item::class.java)
        supportActionBar?.title = selectedBook.volumeInfo.title
        bookDisplay(selectedBook)
        textInfoDescription.setOnClickListener {
            selectedBook.volumeInfo.description?.let { it1 ->
                displayDetails(
                        it1
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate info activity menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Action the menu button choices
        return when (item.itemId) {
            R.id.action_favourite -> {
                val selectedBookInfo = intent.getStringExtra("EXTRA_bookInfo")
                val selectedBook = Gson().fromJson(selectedBookInfo, Book.Item::class.java)
                saveBookInfo(selectedBook)
                true
            }
            R.id.action_remove -> {
                val selectedBookInfo = intent.getStringExtra("EXTRA_bookInfo")
                val selectedBook = Gson().fromJson(selectedBookInfo, Book.Item::class.java)
                removeBookInfo(selectedBook)
                true
            }
            R.id.action_share -> {
                val selectedBookInfo = intent.getStringExtra("EXTRA_bookInfo")
                val selectedBook = Gson().fromJson(selectedBookInfo, Book.Item::class.java)
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_SUBJECT, selectedBook.volumeInfo.title)
                share.putExtra(Intent.EXTRA_TEXT, selectedBook.volumeInfo.canonicalVolumeLink)
                startActivity(Intent.createChooser(share, "Sharing the book!"))
                true
            }
            R.id.action_web -> {
                val selectedBookInfo = intent.getStringExtra("EXTRA_bookInfo")
                val selectedBook = Gson().fromJson(selectedBookInfo, Book.Item::class.java)
                val url = selectedBook.volumeInfo.canonicalVolumeLink
                val intent: Intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(url)
                }
                startActivity(intent)
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

    private fun bookDisplay(selectedBook: Book.Item) {
        val identifierString = StringBuilder()
        val categoryString = StringBuilder()
        val authorString = StringBuilder()

        Picasso.get().load(intent.getStringExtra("EXTRA_modImageURL"))
                .placeholder(R.drawable.book_pholder)
                .resize(486, 783)
                .centerInside()
                .into(imageInfoCover)
        textInfoTitle.text = selectedBook.volumeInfo.title

        //Iterate through potential list of authors and display appropriately
        if (selectedBook.volumeInfo.authors != null) {
            if (selectedBook.volumeInfo.authors.size > 3) {
                for (i in 0 until 3) {
                    if (selectedBook.volumeInfo.authors[i].length > 30) {
                        authorString.append(selectedBook.volumeInfo.authors[i].substring(0, 30) + "...\n")
                    } else {
                        authorString.append(selectedBook.volumeInfo.authors[i]).append('\n')
                    }
                }
                authorString.append("Et al.")
            } else {
                for (i in selectedBook.volumeInfo.authors.indices) {
                    if (selectedBook.volumeInfo.authors[i].length > 30) {
                        authorString.append(selectedBook.volumeInfo.authors[i].substring(0, 30) + "...\n")
                    } else {
                        if (i < selectedBook.volumeInfo.authors.size - 1) {
                            authorString.append(selectedBook.volumeInfo.authors[i]).append('\n')
                        } else {
                            authorString.append(selectedBook.volumeInfo.authors[i])
                        }
                    }
                }
            }
            textInfoAuthor.text = authorString.toString()
        }

        //Check publisher content and display appropriately
        textInfoPublishDate.text = selectedBook.volumeInfo.publishedDate.replace('-', '/')
        if (selectedBook.volumeInfo.description?.isEmpty() == true) {
            textInfoDescription.text = "No description provided."
        } else {
            textInfoDescription.text = selectedBook.volumeInfo.description
        }
        if (selectedBook.volumeInfo.publisher?.isEmpty() == true) {
            textInfoPublisher.text = "Unknown"
        } else {
            textInfoPublisher.text = selectedBook.volumeInfo.publisher
        }

        textInfoPages.text = selectedBook.volumeInfo.pageCount.toString()
        textInfoRating.text = selectedBook.volumeInfo.averageRating.toString()
        textInfoUserRateCount.text = selectedBook.volumeInfo.ratingsCount.toString()

        //Format the book identification numbers and display
        if (selectedBook.volumeInfo.industryIdentifiers != null) {
            for (i in selectedBook.volumeInfo.industryIdentifiers.indices) {
                if (i < selectedBook.volumeInfo.industryIdentifiers.size - 1) {
                    //Format to look appropriate
                    val typeFormat = selectedBook.volumeInfo.industryIdentifiers[i].type.replace('_', ' ')
                    //Build the string from both parts of the index
                    identifierString.append(typeFormat).append(": ").append(selectedBook.volumeInfo.industryIdentifiers[i].identifier).append('\n')
                } else {
                    val typeFormat = selectedBook.volumeInfo.industryIdentifiers[i].type.replace('_', ' ')
                    identifierString.append(typeFormat).append(": ").append(selectedBook.volumeInfo.industryIdentifiers[i].identifier)
                }
            }
            textInfoISBN.text = identifierString
        } else {
            textInfoISBN.text = "Unknown"
        }

        //Format the category list and display
        if (selectedBook.volumeInfo.categories != null) {
            for (i in selectedBook.volumeInfo.categories.indices) {
                if (i < selectedBook.volumeInfo.categories.size - 1) {
                    categoryString.append(selectedBook.volumeInfo.categories[i]).append(", ")
                } else {
                    categoryString.append(selectedBook.volumeInfo.categories[i])
                }
            }
            textInfoCategory.text = categoryString
        } else {
            textInfoCategory.text = "Not specified"
        }

        //Display maturity rating in sensible format
        if (selectedBook.volumeInfo.maturityRating == "MATURE") {
            textInfoMaturity.text = "Rated mature"
        } else {
            textInfoMaturity.text = "Not rated mature"
        }
    }

    private fun saveBookInfo(selectedBook: Book.Item) {
        //Handle file storage for saving favourite book to device storage
        try {
            val fileBooks = File(filesDir, "savedbooks.txt")
            var entryExist = false
            //Check if the book already exists
            if (fileBooks.exists()) {
                val checkBook = Gson().toJson(selectedBook)
                Log.d("CHECKBOOK READ", checkBook.toString())
                val inputStream: InputStream = File(filesDir, "savedbooks.txt").inputStream()
                Log.d("File exists?", "TRUE")
                inputStream.bufferedReader().forEachLine {
                    val currentLine = it
                    if (checkBook == currentLine) {
                        Toast.makeText(this, "Already saved!", Toast.LENGTH_SHORT).show()
                        entryExist = true
                    } else {
                        Log.d("LINE READ", inputStream.toString())
                    }
                }
                inputStream.close()
            //If not then write book details to file
            }
            if (!entryExist) {
                val fileOutputStream: FileOutputStream = openFileOutput("savedbooks.txt", Context.MODE_APPEND)
                val bookWriter = OutputStreamWriter(fileOutputStream)
                val saveSelectedBook = Gson().toJson(selectedBook)
                bookWriter.write(saveSelectedBook)
                bookWriter.write("\n")
                bookWriter.close()
                Log.d("Book was written as", saveSelectedBook)
                Toast.makeText(this, "Saved to bookshelf!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun removeBookInfo(selectedBook: Book.Item) {
        //Handle file storage for removing favourite book from device storage
        try {
            val fileBooks = File(filesDir, "savedbooks.txt")
            val temp = File(filesDir, "temp.txt")
            val bw = BufferedWriter(FileWriter(temp))
            val checkBook = Gson().toJson(selectedBook)
            var entryExist = false
            //Check line by line for existing book entry match
            fileBooks.bufferedReader().forEachLine {
                //If not matched, write entry to temp file and scan next line
                if (checkBook != it) {
                    bw.write(it + '\n')
                }
                else {
                    //Match found
                    Toast.makeText(this, "Removed from shelf!", Toast.LENGTH_SHORT).show()
                    entryExist = true
                }
            }
            //Close writer and delete old file once file has been processed, rename temp file to original
            bw.close()
            fileBooks.delete()
            val delete = fileBooks.delete()
            Log.d("DELETE", delete.toString())
            val rename = temp.renameTo(fileBooks)
            Log.d("RENAME", rename.toString())
            //If not found
            if (!entryExist) {
                Toast.makeText(this, "Not found on shelf!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Not found on shelf!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayDetails(description: String) {
        //On description press, pop out the full text
        AlertDialog.Builder(this@BookInfoActivity)
                .setTitle("Description")
                .setMessage(description)
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .setIcon(R.drawable.ic_book_description)
                .show()
    }

}