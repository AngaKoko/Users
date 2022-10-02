package com.angakoko.vpdmoney.main

import android.app.Activity
import android.app.Application
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var movieRepo = DBUserRepository()
    private val userDao = UserDatabase.getInstance(application).userDao()

    private val header: MutableLiveData<String> = MutableLiveData()
    fun getHeader(): MutableLiveData<String> = header
    fun setHeader(string: String){header.value = string}

    private val user: MutableLiveData<User> = MutableLiveData()
    fun getUser(): MutableLiveData<User> = user
    fun setUser(u: User){user.value = u}

    private val message: MutableLiveData<String> = MutableLiveData()
    fun setMessage(string: String){message.value = string}
    fun getMessage(): MutableLiveData<String> = message

    fun getUsers(db: UserDatabase): Flow<PagingData<User>> {
        return movieRepo.getPopularMovies(db).cachedIn(viewModelScope)
    }

    init {
        getUsers()
    }

    //Add new user to local DB
    fun insetUserInDb(user: User){
        uiScope.launch {
            insertUser(user)
        }
    }

    private suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
        setMessage("New user added")
        activity.onBackPressed()
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