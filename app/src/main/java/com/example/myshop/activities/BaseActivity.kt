package com.example.myshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myshop.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackbar= Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)

    }

}
