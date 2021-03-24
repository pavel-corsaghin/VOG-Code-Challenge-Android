package com.example.fcmexample.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner


inline fun <reified T: ViewModel> ViewModelStoreOwner.viewModel(): Lazy<T> = lazy { ViewModelProvider(this).get(T::class.java)}