package com.twiceyuan.safetyparams.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.twiceyuan.safetyparams.R
import com.twiceyuan.safetyparams.databinding.ActivityReceiverBinding
import com.twiceyuan.safetyparams.library.FragmentParams
import com.twiceyuan.safetyparams.library.parseParams
import com.twiceyuan.safetyparams.sample.bean.Father
import com.twiceyuan.safetyparams.sample.bean.ParcelableBean

class ReceiverFragment : DialogFragment() {

    data class Params(
            val name: String,
            val phone: String,
            val emails: ArrayList<String>?,
            val parcelableBean: ParcelableBean,
            val nestedBean: Father,
            val age: Int
    ) : FragmentParams<ReceiverFragment>()
    private val params by parseParams<Params>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_receiver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ActivityReceiverBinding.bind(view)
        binding.tvContent.text = params.toJsonTree()
    }
}