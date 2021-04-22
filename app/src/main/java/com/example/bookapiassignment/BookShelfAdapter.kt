package com.example.bookapiassignment


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_shelf_display.view.*

class BookShelfAdapter(private val books: ArrayList<Book.Item>): RecyclerView.Adapter<BookShelfAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookShelfAdapter.ViewHolder {
        return BookShelfAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.book_shelf_display, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var stringMod: String
        val theBook = books[position]

        holder.title.text = theBook.volumeInfo.title
        holder.author.text = theBook.volumeInfo.authors?.get(0)

        stringMod = if (theBook.volumeInfo.imageLinks != null) {
            theBook.volumeInfo.imageLinks.thumbnail
        }
        else {
            "None"
        }
        stringMod = stringMod.substring(0, 4) + "s" + stringMod.substring(4, stringMod.length)
        Picasso.get().load(stringMod)
            .placeholder(R.drawable.book_pholder)
            .resize(486, 783)
            .centerInside()
            .into(holder.cover)
        holder.itemView.setOnClickListener {
            val bookInfoIntent = Intent(holder.itemView.context, BookInfoActivity::class.java)
            bookInfoIntent.putExtra("EXTRA_bookInfo", Gson().toJson(theBook))
            bookInfoIntent.putExtra("EXTRA_modImageURL", stringMod)
            holder.itemView.context.startActivity(bookInfoIntent)
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val author = view.textShelfAuthor
        val title = view.textShelfTitle
        val cover = view.imageShelfCover
    }
}