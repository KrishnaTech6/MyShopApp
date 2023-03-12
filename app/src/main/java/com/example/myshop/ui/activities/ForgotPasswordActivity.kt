package com.example.myshop.ui.activities

import android.os.Bundle
import android.widget.Toast
import com.example.myshop.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        setupActionBar()
        supportActionBar?.title=""  //to hide annoying myshop title from toolbar


        btn_submit.setOnClickListener {
            val email: String = et_email_login.text.toString().trim { it <= ' ' }

            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_email), true)
            }
            else{
                showDialogProgress(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener{ task ->
                        hideProgressDialog()
                        if (task.isSuccessful){
                            hideProgressDialog()
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Email sent successfully to reset your Password! ",
                                Toast.LENGTH_LONG)
                                .show()
                            finish()
                        }
                        else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }

            }
        }


    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_forgot_activity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        }
        toolbar_forgot_activity.setNavigationOnClickListener { onBackPressed() }
    }
}