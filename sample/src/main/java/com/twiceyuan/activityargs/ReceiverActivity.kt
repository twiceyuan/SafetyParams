package com.twiceyuan.activityargs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.twiceyuan.activityargs.bean.Father
import com.twiceyuan.activityargs.bean.ParcelableBean
import kotlinx.android.synthetic.main.activity_receiver.*
import org.json.JSONObject


/**
 * Created by twiceYuan on 2018/3/27.
 *
 * 接收参数的 Activity
 */
class ReceiverActivity : AppCompatActivity() {

    data class Starter(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : ActivityArgs() {
        override fun targetClass() = ReceiverActivity::class.java
    }

    private val args by lazy { parseActivityArgs<Starter>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        tv_content.text = args.toJsonTree()
    }

    private fun Any.toJsonTree() = JSONObject(Gson().toJson(this)).toString(2) ?: "{}"
}

