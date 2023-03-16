package com.example.myshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionbar()
        supportActionBar?.title= ""
    }

    private fun setupActionbar(){
        setSupportActionBar(toolbar_add_product)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_add_product.setNavigationOnClickListener { onBackPressed() }
    }
}