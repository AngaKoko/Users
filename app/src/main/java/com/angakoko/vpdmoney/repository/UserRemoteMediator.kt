package com.angakoko.vpdmoney.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.angakoko.vpdmoney.api.UserApi
import com.angakoko.vpdmoney.db.UserDatabase
import com.angakoko.vpdmoney.model.User
import com.angakoko.vpdmoney.model.toUser
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class UserRemoteMediator(private val dB: UserDatabase) : RemoteMediator<Int, User>() {

    private val userDao = dB.userDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // Query DB for SubredditRemoteKey for the subreddit.
                    // SubredditRemoteKey is a wrapper object we use to keep track of page keys we
                    // receive from the Reddit API to fetch the next or previous page.

                    // We must explicitly check if the page key is null when appending, since the
                    // Reddit API informs the end of the list by returning null for page key, but
                    // passing a null key to Reddit API will fetch the initial page.

                    //since endpoint is not paged
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val result = UserApi.retrofitService.getUserAsync()

            //Response
            val userRepository = result.await()
            val items = mutableListOf<User>()
            for (value in userRepository){
                items.add(value.toUser())
            }

            dB.withTransaction {
                //Check if load type is refresh
                if (loadType == LoadType.REFRESH) {
                    //if refresh delete all users in DB
                    userDao.deleteAll()
                }
                //insert list of users in DB
                userDao.insert(items)
            }

            //Always true
            return MediatorResult.Success(endOfPaginationReached = true)

        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}