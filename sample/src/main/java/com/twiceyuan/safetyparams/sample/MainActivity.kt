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

        // 普通传值给 Activity
        btn_launch_by_fun.setOnClickListener {
            val intent = Intent(this, ReceiverActivity::class.java)
            intent.putExtra("name", "twiceYuan")
            intent.putExtra("phone", "000000000")
            intent.putExtra("emails", arrayListOf("somemail1@gmail.com, somemail2@gmail.com"))
            intent.putExtra("age", 0)
            intent.putExtra("parcelableBean", ParcelableBean("twiceYuan", 100))
            intent.putExtra("nestedBean", Father("Father", 25, Child("child", 1)))
            startActivity(intent)
        }

        // 使用 SafetyParams 传值给 Activity
        btn_launch_by_starter.setOnClickListener {
            ReceiverActivity.Params(
                    age = 25,
                    name = "Tony",
                    phone = "123456789",
                    parcelableBean = ParcelableBean("Tony", 100),
                    emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
                    nestedBean = Father("Father", 25, Child("child", 1))
            ).launch(this)
        }

        // 使用 SafetyParams 传值给 Fragment
        btn_launch_fragment.setOnClickListener {
            val dialog = ReceiverFragment.Params(
                    age = 25,
                    name = "Tony",
                    phone = "123456789",
                    parcelableBean = ParcelableBean("Tony", 100),
                    emails = arrayListOf("somemail1@gmail.com, somemail2@gmail.com"),
                    nestedBean = Father("Father", 25, Child("child", 1))
            ).newInstance()
            dialog.show(supportFragmentManager, null)
        }
    }
}