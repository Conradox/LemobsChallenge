package com.example.lemobschallenge.view.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.lemobschallenge.view.MainViewModel

import com.example.lemobschallenge.databinding.FragmentBookshelfBinding

class BookshelfFragment : Fragment() {
    lateinit var viewModel: MainViewModel
    lateinit var binding: FragmentBookshelfBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        activity?.let{
            viewModel = ViewModelProvider(it).get(MainViewModel::class.java)
        }

        binding = FragmentBookshelfBinding.inflate(layoutInflater)

        val view = binding.root

        binding.bookshelfRecycleview.layoutManager = GridLayoutManager(activity, 3)

        viewModel.purchasedBooks.observe(viewLifecycleOwner, Observer {
            binding.bookshelfRecycleview.adapter = BookshelfItemAdapter(it)
        })

        viewModel.walletValue.observe(viewLifecycleOwner, Observer {
            showWallet()
        })

        return view
    }

    private fun showWallet()
    {
        binding.walletValue.text = "%.2f".format(viewModel.walletValue.value).replace(".", ",")
    }

}

