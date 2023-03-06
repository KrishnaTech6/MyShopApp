package com.example.myshop.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.myshop.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {
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
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when (view.id){
                R.id.tv_forgot_password-> {}
                R.id.btn_login -> {
                    validateLoginDetails()
                }

                R.id.tv_register -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
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
                showErrorSnackBar(resources.getString(R.string.login_successful), false)
                true
            }

        }
    }

}