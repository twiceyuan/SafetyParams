package com.twiceyuan.safetyparams.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twiceyuan.safetyparams.databinding.ActivityReceiverBinding
import com.twiceyuan.safetyparams.library.ActivityParams
import com.twiceyuan.safetyparams.library.parseParams
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean

/**
 * Created by twiceYuan on 2018/3/27.
 *
 * 接收参数的 Activity
 */
class ReceiverActivity : AppCompatActivity() {

    data class Params(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : ActivityParams<ReceiverActivity>()

    private val args by parseParams<Params>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReceiverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvContent.text = args.toJsonTree()
    }
}

