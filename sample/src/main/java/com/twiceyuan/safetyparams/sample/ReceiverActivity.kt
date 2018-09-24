package com.twiceyuan.safetyparams.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.twiceyuan.safetyparams.R
import com.twiceyuan.safetyparams.library.SafetyParams
import com.twiceyuan.safetyparams.library.parseParams
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean
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
    ) : SafetyParams(ReceiverActivity::class.java)

    private val args by lazy { parseParams<Starter>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiver)

        tv_content.text = args.toJsonTree()
    }

    private fun Any.toJsonTree() = JSONObject(Gson().toJson(this)).toString(2) ?: "{}"
}

