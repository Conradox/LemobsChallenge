package com.example.lemobschallenge.view.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.lemobschallenge.MainViewModel

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

        //sharedPreferences.edit().clear().apply()
        binding = FragmentBookshelfBinding.inflate(layoutInflater)

        val view = binding.root

        binding.bookshelfRecycleview.layoutManager = GridLayoutManager(activity, 3)

        /*
        viewModel.purchasedBooks.value?.let{

        }
        */

        viewModel.purchasedBooks.observe(viewLifecycleOwner, Observer {
            binding.bookshelfRecycleview.adapter = BookshelfItemAdapter(it)
            //binding.bookshelfRecycleview.adapter?.notifyDataSetChanged()
        })

        viewModel.walletValue.observe(viewLifecycleOwner, Observer {
            showWallet()
        })

        return view
    }

    fun showWallet()
    {
        binding.walletValue.text = "%.2f".format(viewModel.walletValue.value).replace(".", ",")
    }


}
/*
    override fun onResume() {
        super.onResume()
        viewModel.getAllData()
    }
*/


