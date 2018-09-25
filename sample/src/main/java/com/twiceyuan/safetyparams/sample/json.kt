package com.twiceyuan.safetyparams.sample

import com.google.gson.Gson
import org.json.JSONObject

fun Any.toJsonTree() = JSONObject(Gson().toJson(this)).toString(2) ?: "{}"