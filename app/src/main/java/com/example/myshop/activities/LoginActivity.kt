package com.example.myshop.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import com.example.myshop.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //to hide actionbar
        supportActionBar?.hide()
        //to hide status_bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            //for lower version of Android
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            userLogin()
        }
        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }

    }


        private fun validateLoginDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_email_login.text.toString().trim{ it <= ' '}) ->{  // to trim trailing and leading spaces
                showErrorSnackBar(resources.getString(R.string.err_email), true)
                false }

            TextUtils.isEmpty(et_password_login.text.toString().trim{ it <= ' '}) ->{  // to trim trailing and leading spaces
                showErrorSnackBar(resources.getString(R.string.err_password), true)
                false }

            else ->{
                //showErrorSnackBar(resources.getString(R.string.login_successful), false)
                true
            }

        }
    }

    private fun userLogin(){

        if (validateLoginDetails()){
            showDialogProgress(resources.getString(R.string.please_wait))

            val email: String = et_email_login.text.toString().trim { it <= ' ' }
            val password: String = et_password_login.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    hideProgressDialog()
                    if (task.isSuccessful){

                        //TODO- send user to MainActivity
                        showErrorSnackBar("You are Logged in Successfully", false)
                    }
                    else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }

    }




}