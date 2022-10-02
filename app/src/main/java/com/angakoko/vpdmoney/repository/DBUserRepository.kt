package com.angakoko.vpdmoney.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.angakoko.vpdmoney.db.UserDatabase
import com.angakoko.vpdmoney.model.User
import kotlinx.coroutines.flow.Flow


class DBUserRepository {

    @OptIn(ExperimentalPagingApi::class)
    fun getPopularMovies(dB: UserDatabase): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            remoteMediator = UserRemoteMediator(dB)
        ){
            dB.userDao().getUsers()
        }.flow
    }
}