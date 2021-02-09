package com.example.lemobschallenge.pages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lemobschallenge.R
import com.example.lemobschallenge.model.Book
import com.squareup.picasso.Picasso

class BookshelfItemAdapter (val books: MutableList<Book>) : RecyclerView.Adapter<BookshelfItemAdapter.BookshelfItemHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookshelfItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookshelf_item, parent, false)
        return BookshelfItemHolder(view)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookshelfItemHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class BookshelfItemHolder(val itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(book: Book)
        {
            itemView.findViewById<TextView>(R.id.book_title).text    = book.title
            itemView.findViewById<TextView>(R.id.book_writer).text   = book.writer
            Picasso.with(itemView.context).load(book.imageUrl).into(itemView.findViewById<ImageView>(R.id.book_image))
        }
    }
}