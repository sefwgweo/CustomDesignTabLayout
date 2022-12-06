package com.example.customizeddesigntablayout.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.customizeddesigntablayout.R
import com.example.customizeddesigntablayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val fragment = TabFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
}