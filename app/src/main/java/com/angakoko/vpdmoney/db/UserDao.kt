package com.angakoko.vpdmoney.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.angakoko.vpdmoney.model.User

@Dao
interface UserDao {

    //insert list of items item in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item:List<User>)

    //Insert an item into DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)

    //Query an items from DB
    @Query("SELECT * FROM user_table")
    fun getUsers(): PagingSource<Int, User>

    //For test purpose
    @Query("SELECT * FROM user_table LIMIt 5")
    suspend fun getAllUsers(): List<User>

    //Delete all items from DB
    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

}