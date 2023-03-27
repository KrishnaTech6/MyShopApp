package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() , View.OnClickListener{
    private lateinit var userDetails : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        setUpActionBar()
        supportActionBar?.title = ""


        btn_logout.setOnClickListener(this)
        edit_settings.setOnClickListener(this)
        ll_addresses.setOnClickListener(this)


    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_settings_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)
    }

     fun userDetailsSuccess(user: User){
        hideProgressDialog()

         userDetails = user

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_profile_photo)

        name_settings.text = "${user.firstName} ${user.lastName}"
        gender_settings.text = user.gender
        email_settings.text = user.email
        phone_settings.text ="${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(view: View?) {

        if (view!=null){
            when(view.id){
                R.id.btn_logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK  //TO clear out all the existing activities and start again
                    startActivity(intent)

                }

                R.id.edit_settings -> {
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, userDetails)
                    startActivity(intent)
                }
                R.id.ll_addresses -> {
                    val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
                    startActivity(intent)
                }

            }

        }


    }


}