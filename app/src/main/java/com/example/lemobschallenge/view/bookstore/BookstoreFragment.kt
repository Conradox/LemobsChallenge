package com.example.lemobschallenge.view.bookstore

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lemobschallenge.MainViewModel
import com.example.lemobschallenge.model.Book
import com.example.lemobschallenge.databinding.FragmentBookstoreBinding

class BookstoreFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentBookstoreBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        activity?.let{
            viewModel = ViewModelProvider(it).get(MainViewModel::class.java)
        }

        binding = FragmentBookstoreBinding.inflate(layoutInflater)

        val view = binding.root

        binding.bookstoreRecycleview.layoutManager = GridLayoutManager(activity, 2)
        viewModel.showedBooks.value?.let{
            binding.bookstoreRecycleview.adapter = BookstoreItemAdapter(it, ::buyBook, ::favoriteButton, ::showImage)
        }

        viewModel.availableBooks.observe(viewLifecycleOwner, Observer {
            viewModel.createShowedBooks()
        })

        viewModel.showedBooks.observe(viewLifecycleOwner, Observer {
            binding.bookstoreRecycleview.adapter?.notifyDataSetChanged()
        })

        viewModel.walletValue.observe(viewLifecycleOwner, Observer {
            showWallet()
        })

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
            viewModel.getAvailableBooks()
        }
        viewModel.showedBooks.value = viewModel.showedBooks.value
        Log.d("Debugging", viewModel.showedBooks.value?.size.toString())

        return view

    }

    private fun buyBook(book : Book) {
        if (viewModel.buyBook(book) == 1)
        {
            Toast.makeText(activity, "Compra realizada com sucesso!", Toast.LENGTH_SHORT).show()
        }else
        {
            Toast.makeText(activity, "Saldo insuficiente!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showWallet()
    {
        binding.walletValue.text = "%.2f".format(viewModel.walletValue.value).replace(".", ",")
    }

    fun favoriteButton(book : Book)
    {
        viewModel.favoriteButton(book)
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
