package com.example.myshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        setUpActionBar()
        supportActionBar?.title=""
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_address_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_address_activity.setNavigationOnClickListener { onBackPressed() }

    }

}