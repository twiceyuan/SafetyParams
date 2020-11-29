package com.twiceyuan.safetyparams.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.twiceyuan.safetyparams.R
import com.twiceyuan.safetyparams.databinding.ActivityReceiverBinding
import com.twiceyuan.safetyparams.library.SafetyFragmentParams
import com.twiceyuan.safetyparams.library.parseParams
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean

class ReceiverFragment : DialogFragment() {

    data class Params(
            val age: Int,
            val name: String,
            val phone: String,
            val nestedBean: Father,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean
    ) : SafetyFragmentParams<ReceiverFragment>(ReceiverFragment::class.java)

    private val params by lazy { parseParams<Params>() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_receiver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ActivityReceiverBinding.bind(view)
        binding.tvContent.text = params.toJsonTree()
    }
}