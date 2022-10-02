package com.angakoko.vpdmoney.model

fun UserResponse.toUser(): User{
    return User(
        id = this.id,
        name = this.name,
        username = this.username,
        email = email,
        street = address.street,
        suite = address.suite,
        city = address.city,
        zipcode = address.zipcode,
        company = company.companyName,
        catchPhrase = company.catchPhrase,
        bs = company.bs,
        lat = address.geo.lat,
        lng = address.geo.lat,
    )
}