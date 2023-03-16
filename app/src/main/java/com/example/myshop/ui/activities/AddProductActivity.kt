package com.example.myshop.ui.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.activity_add_product.*

class AddProductActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionbar()
        supportActionBar?.title= ""

        iv_product_image.setOnClickListener(this)
    }

    private fun setupActionbar(){
        setSupportActionBar(toolbar_add_product)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_add_product.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v!=null ){
            when (v.id){
                R.id.iv_product_image ->{
                    if(ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){

                        Constants.showImageChooser(this@AddProductActivity)
                    }
                    else{
                        ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
            }
        }

    }
}