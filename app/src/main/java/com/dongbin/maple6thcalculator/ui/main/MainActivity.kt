package com.dongbin.maple6thcalculator.ui.main

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dongbin.maple6thcalculator.R
import com.dongbin.maple6thcalculator.databinding.ActivityMainBinding
import com.dongbin.maple6thcalculator.core.BaseActivity
import com.dongbin.maple6thcalculator.data.AppDatabase
import com.dongbin.maple6thcalculator.data.UserRepository
import com.dongbin.maple6thcalculator.util.calculate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var viewModel: MainViewModel
    private val db: AppDatabase by lazy { AppDatabase.getInstance(this) }
    private lateinit var userName: String
    private lateinit var repository: UserRepository

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.check()
        }
    }

    override fun init() {
        repository = UserRepository(db.userDao())
        viewModel = ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(repository)
        )[MainViewModel::class.java]

        binding.apply {
            viewModel = this@MainActivity.viewModel
        }

        viewModel.loadable.observe(this) {
            if (it != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    userName = it.toString()
                    viewModel.load(userName)
                }
            } else {
                Toast.makeText(this, "유저정보를 등록해보세요", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.insert.observe(this) {
            if (it) {
                Toast.makeText(this, "유저정보 저장에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.load(userName)
                }
            } else {
                Toast.makeText(this, "유저정보 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadUser.observe(this) {
            if (it != null) {
                binding.user = it
                val bitmap = BitmapFactory.decodeByteArray(it.image, 0, it.image.size)
                binding.ivCharacterImage.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "회원정보를 불러오지 못하였습니다", Toast.LENGTH_SHORT).show()
            }
        }

        initView()
    }

    private fun initView() = with(binding) {
        btSearchName.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                userName = etSearchName.text.toString()
                viewModel?.parse(userName)
            }
        }

        btCalculate.setOnClickListener {
            val result = calculate(
                Integer.parseInt(etNowOriginLevel.text.toString()),
                Integer.parseInt(etGoalOriginLevel.text.toString()),
                Integer.parseInt(etNowMasteryLevel.text.toString()),
                Integer.parseInt(etGoalMasteryLevel.text.toString()),
                Integer.parseInt(etNowEnhance1Level.text.toString()),
                Integer.parseInt(etGoalEnhance1Level.text.toString()),
                Integer.parseInt(etNowEnhance2Level.text.toString()),
                Integer.parseInt(etGoalEnhance2Level.text.toString()),
                Integer.parseInt(etNowEnhance3Level.text.toString()),
                Integer.parseInt(etGoalEnhance3Level.text.toString()),
                Integer.parseInt(etNowEnhance4Level.text.toString()),
                Integer.parseInt(etGoalEnhance4Level.text.toString()),
            )

            tvSoleErda.text = result.erda.toString() + "개"
            tvSoleErdaPiece.text = result.erdaPiece.toString() + "개"
        }
    }
}