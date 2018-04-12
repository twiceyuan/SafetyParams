package com.twiceyuan.activityargs

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twiceyuan.activityargs.bean.ParcelableBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_launch.setOnClickListener {
            MainArgs(
                    name = "Tony",
                    phone = "123456789",
                    emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
                    age = 25,
                    parcelableBean = ParcelableBean("Tony", 100)
            ).launch(this)
        }
    }
}