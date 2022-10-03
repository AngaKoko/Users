package com.angakoko.vpdmoney.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_table", indices = [Index(value = ["id"], unique = true)])
data class User(
    @PrimaryKey var id: Int = 0,
    @ColumnInfo var name: String = "",
    @ColumnInfo var username: String = "",
    @ColumnInfo var email: String = "",
    @ColumnInfo var street: String = "",
    @ColumnInfo var suite: String = "",
    @ColumnInfo var city: String = "",
    @ColumnInfo var zipcode: String = "",
    @ColumnInfo var lat: String = "",
    @ColumnInfo var lng: String = "",
    @ColumnInfo var phone: String = "",
    @ColumnInfo var website: String = "",
    @ColumnInfo var company: String = "",
    @ColumnInfo var catchPhrase: String = "",
    @ColumnInfo var bs: String = "",
    @ColumnInfo var imgUri: String = "",
)