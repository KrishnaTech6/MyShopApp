package com.example.myshop.ui.activities

import android.os.Bundle
import android.text.TextUtils
import com.example.myshop.R
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        setupActionBar()
        supportActionBar?.title = ""

        btn_submit_address.setOnClickListener{
            validateData()
        }
    }

    fun setupActionBar(){
        setSupportActionBar(toolbar_add_edit_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        toolbar_add_edit_address.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateData():Boolean{
        return when{
            TextUtils.isEmpty(et_full_name.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_full_name), true)
                false
            }
            TextUtils.isEmpty(et_phone_number.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_phn_no), true)
                false
            }
            TextUtils.isEmpty(et_address.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_address), true)
                false
            }
            TextUtils.isEmpty(et_pin_code.text.toString().trim{it<=' '})->{
                showErrorSnackBar(resources.getString(R.string.err_msg_please_enter_pincode), true)
                false
            }

            else->{
                showErrorSnackBar(resources.getString(R.string.address_uploaded_successfully), true)
                true
            }

        }
    }
}