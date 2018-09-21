package com.kwang.androidchatapp.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val userEmail: String): Parcelable {
    constructor() : this("","","")
}