package com.example.myshop.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        setupActionBar()
        supportActionBar?.title = ""

        btn_submit_address.setOnClickListener{
            saveAddressToFirestore()
        }

        rg_address_type.setOnCheckedChangeListener{_, checkedId ->
            if (checkedId == R.id.rb_OTHER){
                til_other_details.visibility = View.VISIBLE
            }else{
                til_other_details.visibility = View.VISIBLE
            }
        }

    }

    fun setupActionBar(){
        setSupportActionBar(toolbar_add_edit_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        toolbar_add_edit_address.setNavigationOnClickListener { onBackPressed() }
    }

    private fun saveAddressToFirestore(){
        val fullName = et_full_name.text.toString().trim{it<=' '}
        val phoneNumber = et_phone_number.text.toString().trim{it<=' '}
        val address = et_address.text.toString().trim{it<=' '}
        val pinCode = et_pin_code.text.toString().trim{it<=' '}
        val additionalNote = et_additional_note.text.toString().trim{it<=' '}
        val otherDetails = et_other_details.text.toString().trim{it<=' '}
        if (validateData()){
            //to shoe progress dialog
            showDialogProgress(resources.getString(R.string.please_wait))

            val addressType: String = when {
                rb_home.isChecked-> Constants.HOME
                rb_office.isChecked -> Constants.OFFICE
                else -> Constants.OTHER
            }

            val addressData = Address(FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                pinCode,
                additionalNote,
                addressType,
                otherDetails
            )

            FirestoreClass().saveAddressToFirestore(this, addressData)
        }
    }
    fun successAddressSaveToFirestore(){
        hideProgressDialog()

        Toast.makeText(this@AddEditAddressActivity, resources.getString(R.string.address_uploaded_successfully),
        Toast.LENGTH_SHORT).show()

        finish()

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
            rb_OTHER.isChecked && TextUtils.isEmpty(et_other_details.text.toString().trim{it<=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_please_enter_other_details), true)
                false
            }

            else->{
                true
            }

        }
    }
}