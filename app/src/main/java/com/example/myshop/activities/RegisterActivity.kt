package com.example.myshop.activities
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.example.myshop.R
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
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
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
}