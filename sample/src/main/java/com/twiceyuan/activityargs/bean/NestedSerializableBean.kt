package com.twiceyuan.activityargs.bean

import java.io.Serializable

data class Child(val name: String, val age: Int) : Serializable
data class Father(val name: String, val age: Int, val child: Child) : Serializable