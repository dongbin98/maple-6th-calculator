package com.dongbin.maple6thcalculator.ui.main

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.text.Editable
import android.text.TextWatcher
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
            calcErda()
        }

        etNowOriginLevel.addTextChangedListener(textWatcher)
        etGoalOriginLevel.addTextChangedListener(textWatcher)
        etNowMasteryLevel.addTextChangedListener(textWatcher)
        etGoalMasteryLevel.addTextChangedListener(textWatcher)
        etNowEnhance1Level.addTextChangedListener(textWatcher)
        etGoalEnhance1Level.addTextChangedListener(textWatcher)
        etGoalEnhance2Level.addTextChangedListener(textWatcher)
        etNowEnhance3Level.addTextChangedListener(textWatcher)
        etGoalEnhance3Level.addTextChangedListener(textWatcher)
        etNowEnhance4Level.addTextChangedListener(textWatcher)
        etGoalEnhance4Level.addTextChangedListener(textWatcher)
    }

    @SuppressLint("SetTextI18n")
    private fun calcErda() = with(binding) {
        val result = calculate(
            if (etNowOriginLevel.text.isNullOrBlank() or etNowOriginLevel.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowOriginLevel.text.toString()),
            if (etGoalOriginLevel.text.isNullOrBlank() or etGoalOriginLevel.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalOriginLevel.text.toString()),
            if (etNowMasteryLevel.text.isNullOrBlank() or etNowMasteryLevel.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowMasteryLevel.text.toString()),
            if (etGoalMasteryLevel.text.isNullOrBlank() or etGoalMasteryLevel.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalMasteryLevel.text.toString()),
            if (etNowEnhance1Level.text.isNullOrBlank() or etNowEnhance1Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowEnhance1Level.text.toString()),
            if (etGoalEnhance1Level.text.isNullOrBlank() or etGoalEnhance1Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalEnhance1Level.text.toString()),
            if (etNowEnhance2Level.text.isNullOrBlank() or etNowEnhance2Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowEnhance2Level.text.toString()),
            if (etGoalEnhance2Level.text.isNullOrBlank() or etGoalEnhance2Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalEnhance2Level.text.toString()),
            if (etNowEnhance3Level.text.isNullOrBlank() or etNowEnhance3Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowEnhance3Level.text.toString()),
            if (etGoalEnhance3Level.text.isNullOrBlank() or etGoalEnhance3Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalEnhance3Level.text.toString()),
            if (etNowEnhance4Level.text.isNullOrBlank() or etNowEnhance4Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etNowEnhance4Level.text.toString()),
            if (etGoalEnhance4Level.text.isNullOrBlank() or etGoalEnhance4Level.text.isNullOrEmpty()) 1 else Integer.parseInt(etGoalEnhance4Level.text.toString()),
        )

        tvSoleErda.text = result.erda.toString() + "개"
        tvSoleErdaPiece.text = result.erdaPiece.toString() + "개"
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            calcErda()
        }

        override fun afterTextChanged(s: Editable?) {
            calcErda()
        }
    }
}