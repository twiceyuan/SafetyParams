package com.twiceyuan.safetyparams.sample.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ParcelableBean(val name: String, val number: Int) : Parcelable