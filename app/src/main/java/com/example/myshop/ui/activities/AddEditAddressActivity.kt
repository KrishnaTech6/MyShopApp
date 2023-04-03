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
    private var mAddressDetails: Address? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        setupActionBar()
        supportActionBar?.title = ""
        //tv_toolbar_address.text = resources.getString(R.string.add_address)

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails= intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        if (mAddressDetails !=null){
            //tv_toolbar_address.text = resources.getString(R.string.title_edit_address)

            if(mAddressDetails!!.id.isNotEmpty()){
                btn_submit_address!!.text = resources.getString(R.string.btn_lbl_update)
                tv_toolbar_add_edit_address.text = resources.getString(R.string.title_edit_address)

                et_full_name.setText(mAddressDetails!!.name)
                et_phone_number.setText(mAddressDetails!!.mobileNumber)
                et_address.setText(mAddressDetails!!.address)
                et_pin_code.setText(mAddressDetails!!.pinCode)
                et_additional_note.setText(mAddressDetails!!.additionalNote)

                when(mAddressDetails?.type){
                    Constants.HOME ->
                        rb_home.isChecked = true
                    Constants.OFFICE ->
                        rb_office.isChecked = true
                    else ->{
                        rb_OTHER.isChecked=true
                        til_other_details.visibility = View.VISIBLE
                        et_other_details.setText(mAddressDetails!!.otherDetails)
                    }
                }
            }
        }

        btn_submit_address.setOnClickListener{
            saveAddressToFirestore()
        }

        rg_address_type.setOnCheckedChangeListener{_, checkedId ->
            if (checkedId == R.id.rb_OTHER){
                til_other_details.visibility = View.VISIBLE
            }else{
                til_other_details.visibility = View.GONE
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
            if (mAddressDetails!=null && mAddressDetails!!.id.isNotEmpty()){
                FirestoreClass().editAddressData(this, addressData, mAddressDetails!!.id)
            }else{
                FirestoreClass().saveAddressToFirestore(this, addressData)
            }

        }
    }
    fun successAddressSaveToFirestore(){
        hideProgressDialog()

        val notifySuccessMessage :String = if (mAddressDetails!=null && mAddressDetails!!.id.isNotEmpty()){
            resources.getString(R.string.msg_address_successfully_updated)
        } else{
            resources.getString(R.string.address_uploaded_successfully)
        }

        Toast.makeText(this@AddEditAddressActivity,
            notifySuccessMessage,
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