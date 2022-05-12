package com.jaeyoung1.bmi_chap01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jaeyoung1.bmi_chap01.BusEvent.MemoF1toResultFEventBusItem
import com.jaeyoung1.bmi_chap01.BusEvent.ResultF2toMemoF2BusEventItem
import com.jaeyoung1.bmi_chap01.databinding.ResultFBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ResultF : Fragment() {

    private var heightValue = 0
    private var weightValue = 0

    private var mBinding: ResultFBinding? = null
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = ResultFBinding.inflate(inflater, container, false)
        EventBus.getDefault().register(this)

        val bmi: Double = weightValue / ((heightValue * 0.01) * (heightValue * 0.01))

        val resultText = when {
            bmi >= 40 -> "肥満(4度)"
            bmi >= 35 -> "肥満(3度)"
            bmi >= 30 -> "肥満(2度)"
            bmi >= 25 -> "肥満(1度)"
            bmi > 18.5 -> "普通体重"
            else -> "低体重"
        }

        when (resultText) {
            "肥満(4度)" -> binding.bmi6.visibility = View.VISIBLE
            "肥満(3度)" -> binding.bmi5.visibility = View.VISIBLE
            "肥満(2度)" -> binding.bmi4.visibility = View.VISIBLE
            "肥満(1度)" -> binding.bmi3.visibility = View.VISIBLE
            "普通体重" -> binding.bmi2.visibility = View.VISIBLE
            "低体重" -> binding.bmi1.visibility = View.VISIBLE
        }


        binding.bmiNum.text = String.format("%.2f", bmi) // 소수점 버림
        val bmiNumValue = binding.bmiNum.text.toString()



        binding.addMemoButton.setOnClickListener {

            EventBus.getDefault().postSticky(
                ResultF2toMemoF2BusEventItem(
                    heightValue.toString(),
                    bmiNumValue,
                    resultText,
                    1
                )
            )
            (activity as MainActivity).setFrag(2)

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)  //알아서 실행?
    fun addEvent(event: MemoF1toResultFEventBusItem) {

        heightValue = event.heightEvent
        weightValue = event.weightEvent

    }


}