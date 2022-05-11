package com.jaeyoung1.bmi_chap01

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaeyoung1.bmi_chap01.BusEvent.ResultF2toMemoF2BusEventItem
import com.jaeyoung1.bmi_chap01.Roomdb.AppDatabase
import com.jaeyoung1.bmi_chap01.Roomdb.Contacts
import com.jaeyoung1.bmi_chap01.databinding.Memof2Binding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import java.util.*


class MemoF2 : Fragment() {

    private var mBinding: Memof2Binding? = null
    private val binding get() = mBinding!!

    //

    private var db: AppDatabase? = null
    private var contactsList = mutableListOf<Contacts>()

    private var weightMemo = ""
    private var bmiNumMemo = ""
    private var bmiStringMemo = ""
    private var toMemoF2Code = 0

    private var selectYear = ""
    private var selectMonth = ""

    companion object {
        val c = Calendar.getInstance()
        var currentYear = c.get(Calendar.YEAR)
        var currentMonth = c.get(Calendar.MONTH) + 1
        var allMemoCode = 0
    }
    private val currentMonth2 = c.get(Calendar.MONTH) + 1

    private val currentDay = c.get(Calendar.DAY_OF_MONTH)
    var currentDate = "$currentYear" + "년" + " " + "$currentMonth" + "월"

    private val adapter = MemoAdapter(contactsList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        mBinding = Memof2Binding.inflate(inflater, container, false)

        EventBus.getDefault().register(this)
        binding.addDatePicker.text = currentDate
        binding.addDatePicker.setOnClickListener {
            customDaterPicker()
        }

        //초기화
        db = AppDatabase.getInstance(requireActivity())

        //이전에 저장한 내용 모두 불러와서 추가하기
        val savedContacts = db!!.contactsDao().getAll()
        if (savedContacts.isNotEmpty()) {
            contactsList.addAll(savedContacts)
        }

        // 리사이클러뷰 어탭터 설정

        binding.recyclerview.adapter = adapter
        val layout = LinearLayoutManager(activity)
        binding.recyclerview.layoutManager = layout
        binding.recyclerview.setHasFixedSize(true)



        if (toMemoF2Code == 1) {

            //다이알로그
            val dialogLayout = inflater.inflate(R.layout.custom_dialog, container, false)
            val mBuilder = AlertDialog.Builder(activity).setView(dialogLayout)
            val okButton = dialogLayout.findViewById<Button>(R.id.dialog_button_yes)
            val noButton = dialogLayout.findViewById<Button>(R.id.dialog_button_no)
            val mAlertDialog = mBuilder.show()

            okButton.setOnClickListener {

                val contact =
                    Contacts(
                        weightMemo,
                        bmiNumMemo,
                        bmiStringMemo,
                        "ㅎㅇ",
                        currentYear.toString(),
                        currentMonth2.toString(),
                        currentDay.toString()
                    )

                db?.contactsDao()?.insertAll(contact) //DB에 추가
                contactsList.add(contact) //리스트 추가
                adapter.addItem()
                mAlertDialog.dismiss()

            }

            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }

        }

        allMemoButton()

        return binding.root
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)  //알아서 실행
    fun addEvent(event: ResultF2toMemoF2BusEventItem) {
        weightMemo = event.weightMemoEvent
        bmiNumMemo = event.bmiNumMemoEvent
        bmiStringMemo = event.bmiStringEvent

        toMemoF2Code = (event.toMemoF2CodeEvent)++
    }


    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        mBinding = null

        super.onDestroy()

    }


    private fun allMemoButton() {
        binding.allMemoButton.setOnClickListener {
            allMemoCode = 1
            Log.d("A", "A")
            (activity as MainActivity).setFrag(2)
            allMemoCode = 0

        }
    }


    private fun customDaterPicker() {
        val dialog = AlertDialog.Builder(context).create()
        val inflater = LayoutInflater.from(requireContext())
        val datePickerInflater = inflater.inflate(R.layout.custom_datepicker, null, false)
        val year = datePickerInflater.findViewById<NumberPicker>(R.id.year_datePicker)
        val month = datePickerInflater.findViewById<NumberPicker>(R.id.month_datePicker)
        val cancelButton = datePickerInflater.findViewById<Button>(R.id.cancel_button)
        val saveButton = datePickerInflater.findViewById<Button>(R.id.save_button)


        //순환 안되게 막기 휘리릭
        year.wrapSelectorWheel = false
        month.wrapSelectorWheel = false

        //휘리릭 눌럿을때 editText 설정해제
        year.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        month.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        // 날짜 최소값 설정
        year.minValue = 2022
        month.minValue = 1

        //날짜 최대값 설정
        year.maxValue = 2050
        month.maxValue = 12

        //처음 박혀잇을 숫자 정하기
        year.value = currentYear
        month.value = currentMonth2

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        saveButton.setOnClickListener {
            selectMonth = (month.value).toString()
            selectYear = (year.value.toString())

            currentYear = selectYear.toInt()
            currentMonth = selectMonth.toInt()
            currentDate = "$currentYear 년 $currentMonth 월"
            binding.addDatePicker.text = currentDate

            (context as MainActivity).setFrag(2)
            dialog.dismiss()
        }

        dialog.setView(datePickerInflater)
        dialog.create()
        dialog.show()
    }

}





