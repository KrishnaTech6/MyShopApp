package com.example.myshop.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(
            Constants.MYSHOP_PREFERENCES,
            Context.MODE_PRIVATE)  //will give shared preference package which stores all the data
        val username = sharedPreferences.getString(Constants.LOOGED_IN_USERNAME, "")!!

        tv_main.text = "Welcome $username"

    }
}