package com.twiceyuan.activityargs.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twiceyuan.activityargs.R
import com.twiceyuan.activityargs.sample.bean.Child
import com.twiceyuan.activityargs.sample.bean.Father
import com.twiceyuan.activityargs.sample.bean.ParcelableBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_launch.setOnClickListener {
            ReceiverActivity.Starter(
                    name = "Tony",
                    phone = "123456789",
                    emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
                    age = 25,
                    parcelableBean = ParcelableBean("Tony", 100),
                    nestedBean = Father("Father", 25, Child("child", 1))
            ).launch(this)
        }
    }
}