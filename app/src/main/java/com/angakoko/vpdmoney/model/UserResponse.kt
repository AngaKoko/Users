package com.angakoko.vpdmoney.model

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int = -1,
    @SerializedName("name") val name: String = "",
    @SerializedName("username") val username: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("website") val website: String = "",
    @SerializedName("address") val address: Address = Address(),
    @SerializedName("company") val company: Company = Company(),
)

data class Address(
    @SerializedName("street") val street: String = "",
    @SerializedName("suite") val suite: String = "",
    @SerializedName("city") val city: String = "",
    @SerializedName("zipcode") val zipcode: String = "",
    @SerializedName("geo") val geo: Geo = Geo()
)

data class Company(
    @SerializedName("name") val companyName: String = "",
    @SerializedName("catchPhrase") val catchPhrase: String = "",
    @SerializedName("bs") val bs: String = "",
)

data class Geo(
    @SerializedName("lat") val lat: String = "",
    @SerializedName("lng") val lng: String = "",
)