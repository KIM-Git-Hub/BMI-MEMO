package com.jaeyoung1.bmi_chap01


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jaeyoung1.bmi_chap01.BusEvent.MemoF1toResultFEventBusItem

import com.jaeyoung1.bmi_chap01.databinding.Mainf1Binding
import org.greenrobot.eventbus.EventBus

class MemoF1 : Fragment() {


    private var mBinding: Mainf1Binding? = null
    private val binding get() = mBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // val view = inflater.inflate(R.layout.mainf1, container, false)
        mBinding = Mainf1Binding.inflate(inflater, container, false)

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        binding.memoF1Linear.setOnClickListener {
            imm.hideSoftInputFromWindow(binding.height.windowToken, 0)
            imm.hideSoftInputFromWindow(binding.weight.windowToken, 0)
            binding.height.clearFocus()
            binding.weight.clearFocus()
        }

        binding.result.setOnClickListener {

            if (binding.height.text.isEmpty() || binding.weight.text.isEmpty()) {
                Toast.makeText(activity, "身長と体重を入力してください。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 값이 비어 있으면 뒷 코드 실행 안하고 리스너 탈출
            }


            val height = binding.height.text.toString().toInt()
            val weight = binding.weight.text.toString().toInt()

            EventBus.getDefault().postSticky(MemoF1toResultFEventBusItem(height, weight))
            imm.hideSoftInputFromWindow(binding.height.windowToken, 0)
            imm.hideSoftInputFromWindow(binding.weight.windowToken, 0)
            (activity as MainActivity).setFrag(1)
        }


        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}

