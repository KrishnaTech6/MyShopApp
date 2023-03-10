package com.example.myshop.ui.activities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupActionBar()
        supportActionBar?.title=""  //to hide annoying myshop title from toolbar

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else{
            //for lower version of Android
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        val loginTV: TextView = findViewById(R.id.tv_login_already_have_acc)
        loginTV.setOnClickListener {
            onBackPressed()
        }

        btn_register.setOnClickListener {
            registerUser()
        }


    }



    private fun setupActionBar(){
        setSupportActionBar(toolbar_register)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_base_24)
        }
        toolbar_register.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails():Boolean {
        return when{

            TextUtils.isEmpty(et_first_name.text.toString().trim{ it <= ' '}) ->{  // to trim trailing and leading spaces
                showErrorSnackBar(resources.getString(R.string.err_first_name), true)
                false }

            TextUtils.isEmpty(et_last_name.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_last_name), true)
                false }

            TextUtils.isEmpty(et_email_id.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_email), true)
                false }

            !(et_email_id.text.toString().trim{ it <= ' '}.contains( "@") && et_email_id.text.toString().trim{ it <= ' '}.contains( ".") )->{
                showErrorSnackBar(resources.getString(R.string.invalid_email), true)
                false
            }

                    TextUtils.isEmpty(et_password.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_password), true)
                false }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim{ it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_password), true)
                false }

            (et_password.text.toString().trim{it <= ' '} != et_confirm_password.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_password_not_equal_confirm_pass), true)
                false
            }

            !cb_tandc.isChecked ->{
                showErrorSnackBar(resources.getString(R.string.err_agree_tandc), true)
                false
            }
            else -> {
                //showErrorSnackBar(resources.getString(R.string.msg_thanks_registering), false)
                true
            }
        }
    }

    private fun registerUser(){
        if (validateRegisterDetails()){
            showDialogProgress(resources.getString(R.string.please_wait)) //show progress dialog until something is returned by firebase
            val email: String = et_email_id.text.toString().trim{it<= ' '}
            val password: String = et_password.text.toString().trim{it<= ' '}

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser  = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                et_first_name.text.toString().trim{it <=' '},
                                et_last_name.text.toString().trim{it <=' '},
                                et_email_id.text.toString().trim{it <=' '}
                            )

                            FirestoreClass().registerUser(this@RegisterActivity, user)

                            //showErrorSnackBar("User has been registered ${firebaseUser.uid}", false)
//                           FirebaseAuth.getInstance().signOut()
//                           finish()

                        }
                        else{
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
                )
        }
    }

     fun userRegisterSuccess(){
        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
                    resources.getString(R.string.registered_successfully),
                    Toast.LENGTH_LONG)
            .show()
    }
}