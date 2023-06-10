package com.capstoneproject.meatfyi.model

class User() {
    var id_user: String? = null
    var username: String? = null
    var email: String? = null
    var password: String? = null

    constructor(
        id: String?,
        nama_user: String?,
        email_user: String?,
        password_user: String?
    ) : this() {
        this.id_user = id
        this.username = nama_user
        this.email = email_user
        this.password = password_user
    }
}