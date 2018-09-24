package com.twiceyuan.safetyparams.sample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.twiceyuan.safetyparams.R
import com.twiceyuan.safetyparams.sample.bean.Child
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val starter = ReceiverActivity.Starter(
                name = "Tony",
                phone = "123456789",
                emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
                age = 25,
                parcelableBean = ParcelableBean("Tony", 100),
                nestedBean = Father("Father", 25, Child("child", 1))
        )

        btn_launch_by_starter.setOnClickListener {
            starter.launch(this)
        }

        btn_launch_by_fun.setOnClickListener {
            val intent = Intent(this, ReceiverActivity::class.java)
            intent.putExtra("name", starter.name)
            intent.putExtra("phone", starter.phone)
            intent.putExtra("emails", starter.emails)
            intent.putExtra("age", starter.age)
            intent.putExtra("parcelableBean", starter.parcelableBean)
            intent.putExtra("nestedBean", starter.nestedBean)
            startActivity(intent)
        }
    }
}