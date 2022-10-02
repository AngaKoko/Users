package com.angakoko.vpdmoney.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.angakoko.vpdmoney.api.UserApi
import com.angakoko.vpdmoney.db.UserDatabase
import com.angakoko.vpdmoney.model.User
import com.angakoko.vpdmoney.repository.DBUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val context: Context, application: Application): AndroidViewModel(application) {

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var movieRepo = DBUserRepository()

    private val header: MutableLiveData<String> = MutableLiveData()
    fun getHeader(): MutableLiveData<String> = header
    fun setHeader(string: String){header.value = string}

    fun getUsers(db: UserDatabase): Flow<PagingData<User>> {
        return movieRepo.getPopularMovies(db).cachedIn(viewModelScope)
    }

    init {
        getUsers()
    }

    fun getUsers(){
        uiScope.launch {
            val propertiesDifferConfig = UserApi.retrofitService.getUserAsync()

            try {
                val result = propertiesDifferConfig.await()
                Log.d("shank", "Result = $result")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        //clear all co-routine jobs
        viewModelJob.cancel()
    }
}