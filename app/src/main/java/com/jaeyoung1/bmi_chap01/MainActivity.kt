package com.jaeyoung1.bmi_chap01


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.stetho.Stetho
import com.jaeyoung1.bmi_chap01.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // 전역 변수로 바인딩 객체 선언
    private var mBinding: ActivityMainBinding? = null

    // 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

    private var mBackWait: Long = 0

//test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Stetho.initializeWithDefaults(this) //chrome://inspect
        val actionbar = supportActionBar
        actionbar?.hide()

        //setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //프래그먼트 세팅
        setFrag(0)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.fragHome -> {
                    setFrag(0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.fragMemo -> {
                    setFrag(2)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }


    }

    fun setFrag(fragNum: Int) {
        val ft = supportFragmentManager.beginTransaction()
        when (fragNum) {
            0 -> {
                ft.replace(R.id.frame, MemoF1()).commit()
            }
            1 -> {
                ft.replace(R.id.frame, ResultF()).commit()
            }
            2 -> {
                ft.replace(R.id.frame, MemoF2()).commit()
            }
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackWait >= 2000) {
            mBackWait = System.currentTimeMillis()
            Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }


}

