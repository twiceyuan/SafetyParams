package com.twiceyuan.safetyparams.sample.bean

import java.io.Serializable

data class Father(val name: String, val age: Int, val child: Child) : Serializable