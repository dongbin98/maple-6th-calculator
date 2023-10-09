package com.dongbin.maple6thcalculator.ui.splash

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dongbin.maple6thcalculator.R
import com.dongbin.maple6thcalculator.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

//    private fun startMainActivity() {
//        startActivity(Intent(this, MainActivity::class.java))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, 0, R.anim.fade_in)
//        } else {
//            overridePendingTransition(0, R.anim.fade_in)
//        }
//        finish()
//    }
}