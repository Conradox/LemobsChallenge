package com.example.lemobschallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.lemobschallenge.R
import com.example.lemobschallenge.databinding.MainActivityBinding
import com.example.lemobschallenge.utils.SomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        //sharedPreferences.edit().clear().apply()
        viewModel = ViewModelProvider(this, SomeViewModelFactory(this)).get(MainViewModel::class.java)

        viewModel.getAllData()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(navView, navController)

    }
}