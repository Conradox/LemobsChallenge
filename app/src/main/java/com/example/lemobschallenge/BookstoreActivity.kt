package com.example.lemobschallenge

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lemobschallenge.adapter.BookstoreItemAdapter
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.databinding.BookstoreBinding
import com.github.chrisbanes.photoview.PhotoView

class BookstoreActivity : AppCompatActivity() {
    private lateinit var viewModel : BookstoreViewModel
    lateinit var binding : BookstoreBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        //sharedPreferences.edit().clear().apply()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookstoreViewModel::class.java)
        viewModel.putSharedPreferences(sharedPreferences)

        binding = BookstoreBinding.inflate(layoutInflater)

        val view = binding.root

        setContentView(view)

        viewModel.showedBooks.value?.let{
            binding.bookstoreRecycleview.adapter = BookstoreItemAdapter(it, ::buy_book, ::favorite_buttton, ::showImage)
        }

        binding.bookstoreRecycleview.layoutManager = GridLayoutManager(this, 2)

        viewModel.showedBooks.observe(this, Observer {
            binding.bookstoreRecycleview.adapter?.notifyDataSetChanged()
        })

        viewModel.walletValue.observe(this, Observer {
            showWallet()
        })

        viewModel.getAllData()

        binding.searchTextBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.filterShowedBooks(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Debugging", "BeforeTextChanged")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Debugging", "OnTextChanged")
            }
        } )

        binding.refreshButton.setOnClickListener{
            viewModel.getAllData()
        }

        binding.bookshelfButton.setOnClickListener {
            onBackPressed()
        }

    }

    fun buy_book(book : Book) {
        if (viewModel.buy_book(book) == 1)
        {
            Toast.makeText(this, "Compra realizada com sucesso!", Toast.LENGTH_SHORT).show()
        }else
        {
            Toast.makeText(this, "Saldo insuficiente!", Toast.LENGTH_SHORT).show()
        }
    }

    fun showWallet()
    {
        binding.walletValue.text = "%.2f".format(viewModel.walletValue.value).replace(".", ",")
    }

    fun favorite_buttton(book : Book)
    {
        viewModel.favorite_buttton(book)
        binding.bookstoreRecycleview.adapter?.notifyDataSetChanged()
    }

    fun showImage(book: ImageView)
    {
        binding.imageZoom.setImageDrawable(book.drawable)
        binding.imageZoomContainer.visibility = View.VISIBLE
        binding.imageZoomClose.setOnClickListener{
            closeImage()
        }
    }

    fun closeImage()
    {
        binding.imageZoomContainer.visibility = View.GONE
    }
}
