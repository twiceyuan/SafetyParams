package com.twiceyuan.safetyparams.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twiceyuan.safetyparams.databinding.ActivityMainBinding
import com.twiceyuan.safetyparams.sample.bean.Child
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 普通传值给 Activity
        binding.btnLaunchByFun.setOnClickListener {
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
        binding.btnLaunchByStarter.setOnClickListener {
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
        binding.btnLaunchFragment.setOnClickListener {
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