package com.angakoko.vpdmoney

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.angakoko.vpdmoney.main.MainViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val activity: Activity,
                       private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(activity, application) as T
            else -> throw IllegalArgumentException("Unknown class")
        }
    }
}