package com.example.lemobschallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lemobschallenge.adapter.BookshelfItemAdapter
import com.example.lemobschallenge.databinding.BookshelfBinding

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookshelfActivity : AppCompatActivity() {
    lateinit var viewModel : BookshelfViewModel
    lateinit var binding : BookshelfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
        //sharedPreferences.edit().clear().apply()
        binding = BookshelfBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(BookshelfViewModel::class.java)

        viewModel.putSharedPreferences(sharedPreferences)

        val view = binding.root

        setContentView(view)

        binding.bookshelfRecycleview.layoutManager = GridLayoutManager(this, 3)

        viewModel.purchasedBooks.value?.let{
            binding.bookshelfRecycleview.adapter = BookshelfItemAdapter(it)
        }

        viewModel.purchasedBooks.observe(this, Observer {
            binding.bookshelfRecycleview.adapter?.notifyDataSetChanged()
        })

        viewModel.walletValue.observe(this, Observer {
            showWallet()
        })

        binding.bookstoreButton.setOnClickListener {
            startActivity(Intent(this, BookstoreActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllData()
    }

    fun showWallet()
    {
        binding.walletValue.text = "%.2f".format(viewModel.walletValue.value).replace(".", ",")
    }

}