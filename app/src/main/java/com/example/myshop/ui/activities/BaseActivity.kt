package com.example.myshop.ui.activities

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_dialog.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    private var doubleBackToExitPressedOnce= false

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackbar= Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }
            else{
                snackBarView.setBackgroundColor(
                    ContextCompat.getColor(
                        this@BaseActivity,
                        R.color.colorSnackBarSuccess
                    )
                )
            }

        snackbar.show()

    }


    // custom progress dialog

    fun showDialogProgress(text: String){
        mProgressDialog = Dialog(this)

        mProgressDialog.setContentView(R.layout.progress_dialog)

        mProgressDialog.tv_progress.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog() = mProgressDialog.dismiss()

    fun doubleBackToExit(){

        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce= true

        Toast.makeText(this,
        resources.getString(R.string.please_click_back_again_to_exit),
        Toast.LENGTH_LONG)
            .show()

        Handler(Looper.getMainLooper()).postDelayed({doubleBackToExitPressedOnce =false}, 2000)
    }




}
