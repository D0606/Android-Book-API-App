package com.example.bookapiassignment

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_search_results.view.*
import java.lang.StringBuilder


class BookListAdapter(private val books: Book): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
            return books.items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.book_search_results, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var stringMod : String
        val currentBook = books.items[position]

        //Check for image links or flag
        stringMod = if (currentBook.volumeInfo.imageLinks != null) {
            currentBook.volumeInfo.imageLinks.thumbnail
        }
        else {
            "None"
        }
        val authorString = StringBuilder()
        val identifierString = StringBuilder()
        holder.bookTitle.text = currentBook.volumeInfo.title
        //Display correct amount of authors in case of multiple
        if (currentBook.volumeInfo.authors != null) {
            if (currentBook.volumeInfo.authors.size > 3) {
                for (i in 0 until 3) {
                    if (currentBook.volumeInfo.authors[i].length > 30){
                        authorString.append(currentBook.volumeInfo.authors[i].substring(0, 30) + "...\n")
                    }
                    else {
                        authorString.append(currentBook.volumeInfo.authors[i]).append('\n')
                    }
                }
                authorString.append("Et al.")
            } else {
                for (i in currentBook.volumeInfo.authors.indices) {
                    if (currentBook.volumeInfo.authors[i].length > 30){
                        authorString.append(currentBook.volumeInfo.authors[i].substring(0, 30) + "...\n")
                    }
                    else {
                        if (i < currentBook.volumeInfo.authors.size - 1) {
                            authorString.append(currentBook.volumeInfo.authors[i]).append('\n')
                        } else {
                            authorString.append(currentBook.volumeInfo.authors[i])
                        }
                    }
                }
            }
            holder.bookAuthor.text = authorString.toString()
        }
        else {
            holder.bookAuthor.text = "Unknown author"
        }
        //Display all book ID codes and type
        if (currentBook.volumeInfo.industryIdentifiers != null) {
            for (i in currentBook.volumeInfo.industryIdentifiers.indices) {
                if (i < currentBook.volumeInfo.industryIdentifiers.size - 1) {
                    //Format to look appropriate
                    val typeFormat = currentBook.volumeInfo.industryIdentifiers[i].type.replace('_', ' ')
                    //Build the string from both parts of the index
                    identifierString.append(typeFormat).append(": ").append(currentBook.volumeInfo.industryIdentifiers[i].identifier).append('\n')
                } else {
                    val typeFormat = currentBook.volumeInfo.industryIdentifiers[i].type.replace('_', ' ')
                    identifierString.append(typeFormat).append(": ").append(currentBook.volumeInfo.industryIdentifiers[i].identifier)
                }
            }
            holder.bookISBN.text = identifierString.toString()
        }
        else {
            holder.bookISBN.text = "Unknown"
        }
        //Change provided thumbnail URL to one that Picasso supports by making http to https
        stringMod = stringMod.substring(0, 4) + "s" + stringMod.substring(4, stringMod.length)
        Log.d("String is now", stringMod)
        Picasso.get().load(stringMod)
                .placeholder(R.drawable.book_pholder)
                .resize(400, 700)
                .centerInside()
                .into(holder.bookCover)
        //Set recycler view as selectable
        holder.itemView.setOnClickListener {
            val bookInfoIntent = Intent(holder.itemView.context, BookInfoActivity::class.java)
            bookInfoIntent.putExtra("EXTRA_bookInfo", Gson().toJson(currentBook))
            bookInfoIntent.putExtra("EXTRA_modImageURL", stringMod)
            holder.itemView.context.startActivity(bookInfoIntent)
        }
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val bookTitle = view.textBookNameResult
        val bookAuthor = view.textAuthorResult
        val bookISBN = view.textISBNResult
        val bookCover = view.imageBookCoverResult
    }

}