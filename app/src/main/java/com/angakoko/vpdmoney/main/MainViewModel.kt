package com.angakoko.vpdmoney.main

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.withTransaction
import com.angakoko.vpdmoney.R
import com.angakoko.vpdmoney.api.UserApi
import com.angakoko.vpdmoney.db.UserDatabase
import com.angakoko.vpdmoney.model.User
import com.angakoko.vpdmoney.model.toUser
import com.angakoko.vpdmoney.repository.DBUserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val activity: Activity, application: Application): AndroidViewModel(application) {

    //ViewModel job to help perform co-routine jobs
    private var viewModelJob = Job()
    //coroutine scope
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var movieRepo = DBUserRepository()
    private val db = UserDatabase.getInstance(application)
    private val userDao = db.userDao()

    private val header: MutableLiveData<String> = MutableLiveData()
    fun getHeader(): MutableLiveData<String> = header
    fun setHeader(string: String){header.value = string}

    private val user: MutableLiveData<User> = MutableLiveData()
    fun getUser(): MutableLiveData<User> = user
    fun setUser(u: User){user.value = u}

    private val message: MutableLiveData<String> = MutableLiveData()
    fun setMessage(string: String){message.value = string}
    fun getMessage(): MutableLiveData<String> = message

    private val isRefreshing:MutableLiveData<Boolean> = MutableLiveData(false)
    fun setIsRefreshing(boolean: Boolean){isRefreshing.value = boolean}
    fun getIsRefreshing():MutableLiveData<Boolean> = isRefreshing

    private val listUsers = userDao.getAllUser()
    fun getListUsers() = listUsers

    init {
        uiScope.launch {
            //Check if local DB is empty or not
            val lastUser = userDao.getLastUser()
            if(lastUser == null)queryUsers()
        }
    }

    fun updateUserInDb(user: User){
        uiScope.launch {
            withContext(Dispatchers.IO) {
                userDao.update(user)
            }
        }
    }

    //Add new user to local DB
    fun insetUserInDb(user: User){
        uiScope.launch {
            //get last user from DB
            val lastUser = userDao.getLastUser() ?: User()
            //increment unique user id
            user.id = lastUser.id+1
            //insert user in DB
            insertUser(user)
        }
    }

    private suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
        setMessage(activity.getString(R.string.new_user_added))
        activity.onBackPressed()
    }

    fun queryUsers(){
        uiScope.launch {
            val propertiesDifferConfig = UserApi.retrofitService.getUserAsync()
            setIsRefreshing(true)

            try {
                val result = propertiesDifferConfig.await()

                val items = mutableListOf<User>()
                for (value in result){
                    items.add(value.toUser())
                }

                db.withTransaction {
                    userDao.deleteAll()
                    userDao.insert(items)
                }
                setIsRefreshing(false)

            } catch (e: Exception) {
                setIsRefreshing(false)
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