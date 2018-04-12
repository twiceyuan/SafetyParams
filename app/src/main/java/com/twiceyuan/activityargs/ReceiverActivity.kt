package com.twiceyuan.activityargs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.twiceyuan.activityargs.bean.ParcelableBean
import kotlinx.android.synthetic.main.activity_receiver.*
import org.json.JSONObject


/**
 * Created by twiceYuan on 2018/3/27.
 *
 * 接收参数的 Activity
 */
data class MainArgs(
        val name: String,
        val phone: String,
        val emails: ArrayList<String>?,
        val parcelableBean: ParcelableBean,
        val age: Int
) : ActivityArgs() {
    override fun targetClass() = ReceiverActivity::class.java
}

class ReceiverActivity : AppCompatActivity() {

    private val args by lazy { parseActivityArgs<MainArgs>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        val argJsonPrettify = JSONObject(Gson().toJson(args)).toString(2)

        tv_content.text = argJsonPrettify
    }
}

