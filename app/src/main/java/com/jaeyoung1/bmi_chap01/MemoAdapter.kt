package com.jaeyoung1.bmi_chap01


import android.content.Context.INPUT_METHOD_SERVICE
import android.text.Editable
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jaeyoung1.bmi_chap01.Roomdb.AppDatabase
import com.jaeyoung1.bmi_chap01.Roomdb.Contacts


class MemoAdapter(private val itemList: List<Contacts>) :
    RecyclerView.Adapter<MemoAdapter.ContactsViewHolder>() {

    inner class ContactsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        var db: AppDatabase? = null

        fun bind(contacts: Contacts) {
            val weight = view.findViewById<TextView>(R.id.weightItem)
            val bmiNum = view.findViewById<TextView>(R.id.bmiNumItem)
            val bmiString = view.findViewById<TextView>(R.id.bmiStringItem)
            val dbId = view.findViewById<TextView>(R.id.dbId)
            val deleteButton = view.findViewById<ImageView>(R.id.deleteButton)
            val year = view.findViewById<TextView>(R.id.year)
            val month = view.findViewById<TextView>(R.id.month)
            val day = view.findViewById<TextView>(R.id.day)
            val listItem = view.findViewById<LinearLayout>(R.id.list_item)
            val memoTextView = view.findViewById<TextView>(R.id.memoTextView)
            val memoEditText = view.findViewById<EditText>(R.id.memoEditText)

            weight.text = contacts.weight
            bmiNum.text = contacts.bmiNum
            bmiString.text = contacts.bmiString
            dbId.text = contacts.id.toString()
            year.text = contacts.year
            month.text = contacts.month
            day.text = contacts.day
            memoTextView.text = contacts.memoText

            val context = view.context
            val imm = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            db = AppDatabase.getInstance(context)

            while (true) {
                if (year.text.toString() != MemoF2.currentYear.toString() || month.text.toString() != MemoF2.currentMonth.toString()) {
                    listItem.visibility = View.GONE
                    break
                } else if (year.text.toString() == MemoF2.currentYear.toString() && month.text.toString() == MemoF2.currentMonth.toString()) {
                    listItem.visibility = View.VISIBLE
                    break
                }
            }

            deleteButton.setOnClickListener {
                db?.contactsDao()?.delete(contacts)
                (context as MainActivity).setFrag(2)
            }

            memoTextView.setOnClickListener {
                memoEditText.setText(memoTextView.text.toString())
                memoTextView.visibility = View.GONE
                memoEditText.visibility = View.VISIBLE
                memoEditText.requestFocus()

                imm.showSoftInput(memoEditText, 0) // 키보드 올라옴
            }

            memoEditText.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> // 엔터로 키보드 내리기 & 저장
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {

                    imm.hideSoftInputFromWindow(memoEditText.windowToken, 0) //키보드 내리기

                    memoTextView.text = memoEditText.text

                    memoEditText.visibility = View.GONE
                    memoTextView.visibility = View.VISIBLE
                    val id = dbId.text.toString().toLong()
                    val value = memoEditText.text.toString()

                    db?.contactsDao()?.update(id, value)
                    (context as MainActivity).setFrag(2)


                    return@OnKeyListener true
                }
                false
            })

            val id = dbId.text.toString().toLong()
            if (id == 0L) {
                val value = memoEditText.text.toString()
                db?.contactsDao()?.update(id, value)
                (context as MainActivity).setFrag(2)


            }

        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoAdapter.ContactsViewHolder {
        val inflatedView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ContactsViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val item = itemList[position]


        holder.apply {
            bind(item)
        }
    }


    fun addItem() {
        val testNum: Int = itemList.size - 1
        notifyItemInserted(testNum)

    }


}
