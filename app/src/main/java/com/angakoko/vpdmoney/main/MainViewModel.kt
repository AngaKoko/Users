package com.angakoko.vpdmoney.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(private val context: Context, application: Application): AndroidViewModel(application) {

    private val header: MutableLiveData<String> = MutableLiveData()
    fun getHeader(): MutableLiveData<String> = header
    fun setHeader(string: String){header.value = string}
}