package com.ressphere.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyData(val name: String, val age: Int) : Parcelable