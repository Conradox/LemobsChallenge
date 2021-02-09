package com.example.lemobschallenge.view.bookstore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lemobschallenge.R
import com.example.lemobschallenge.model.Book
import com.squareup.picasso.Picasso

class BookstoreItemAdapter (
        val books: MutableList<Book>,
        val buyBook : (book: Book) -> Unit,
        val favoriteButton : (book: Book) -> Unit,
        val showImage : (book: ImageView) -> Unit) : RecyclerView.Adapter<BookstoreItemAdapter.BookstoreItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookstoreItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookstore_item, parent, false)
        return BookstoreItemViewHolder(view)

    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookstoreItemViewHolder, position: Int) {
        holder.bind(books[position])
    }

    inner class BookstoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(book: Book) {
            itemView.findViewById<TextView>(R.id.book_title).text    = book.title
            itemView.findViewById<TextView>(R.id.book_writer).text   = book.writer
            itemView.findViewById<TextView>(R.id.book_price).text   = "%.2f".format(book.price).replace(".", ",")
            if(book.isFavorite)
            {
                itemView.findViewById<ImageView>(R.id.favorite_image).setImageResource(R.drawable.redfavicon)
            }else{
                itemView.findViewById<ImageView>(R.id.favorite_image).setImageResource(R.drawable.favicon)
            }

            Picasso.with(itemView.context).load(book.imageUrl).into(itemView.findViewById<ImageView>(R.id.book_image))
            itemView.findViewById<ImageView>(R.id.book_image).setOnClickListener{
                showImage(itemView.findViewById<ImageView>(R.id.book_image))
            }

            itemView.findViewById<TextView>(R.id.buy_button).setOnClickListener{
                buyBook(book)
            }

            itemView.findViewById<ImageView>(R.id.favorite_image).setOnClickListener {
                favoriteButton(book)
            }
        }
    }
}