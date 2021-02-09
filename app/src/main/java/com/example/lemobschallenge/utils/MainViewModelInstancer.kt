package com.example.lemobschallenge.utils

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lemobschallenge.MainViewModel

class SomeViewModelFactory(private val someString: AppCompatActivity): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(someString) as T
}
