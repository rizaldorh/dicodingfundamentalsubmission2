package com.dicoding.submission2.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var avatar:String,
    var name: String,
    var username: String,
    var company: String,
    var location: String,
    var repository: String,
    var followers: String,
    var following: String,
) : Parcelable