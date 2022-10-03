package com.angakoko.vpdmoney.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.angakoko.vpdmoney.model.User

@Dao
interface UserDao {

    //insert list of items item in DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item:List<User>)

    //Insert an item into DB
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: User)

    @Update
    suspend fun update(user: User)

    //Query an items from DB
    @Query("SELECT * FROM user_table")
    fun getUsers(): PagingSource<Int, User>

    //Query an items from DB
    @Query("SELECT * FROM user_table")
    fun getAllUser(): LiveData<List<User>>

    //Get last object
    @Query("SELECT * FROM user_table ORDER BY id DESC LIMIt 1")
    suspend fun getLastUser(): User?

    //For test purpose
    @Query("SELECT * FROM user_table LIMIt 5")
    suspend fun getAllUsers(): List<User>

    //Delete all items from DB
    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

}