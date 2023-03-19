package com.example.myshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionbar()
        supportActionBar?.title= ""
    }
    private fun setupActionbar(){
        setSupportActionBar(toolbar_product_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_product_details.setNavigationOnClickListener { onBackPressed() }
    }
}