package com.angakoko.vpdmoney.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", indices = [Index(value = ["ID_"], unique = true)])
data class User(
    @PrimaryKey val ID_: Int = -1,
    @ColumnInfo val name: String = "",
    @ColumnInfo val username: String = "",
    @ColumnInfo val email: String = "",
    @ColumnInfo val street: String = "",
    @ColumnInfo val suite: String = "",
    @ColumnInfo val city: String = "",
    @ColumnInfo val zipcode: String = "",
    @ColumnInfo val lat: String = "",
    @ColumnInfo val lng: String = "",
    @ColumnInfo val phone: String = "",
    @ColumnInfo val website: String = "",
    @ColumnInfo val company: String = "",
    @ColumnInfo val catchPhrase: String = "",
    @ColumnInfo val bs: String = ""
)