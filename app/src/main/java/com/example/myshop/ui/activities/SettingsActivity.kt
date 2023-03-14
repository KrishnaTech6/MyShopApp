package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        setUpActionBar()
        supportActionBar?.title = ""


        edit_settings.setOnClickListener{

            val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetailsSettings(){
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

     fun userDetailsSuccess(user: User){
        hideProgressDialog()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_profile_photo)

        name_settings.text = "${user.firstName} ${user.lastName}"
        gender_settings.text = user.gender
        email_settings.text = user.email
        phone_settings.text ="${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetailsSettings()
    }

}