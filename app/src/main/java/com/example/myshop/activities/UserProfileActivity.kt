package com.example.myshop.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        var userDetails: User = User()

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.isEnabled = false
        et_first_name.setText(userDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(userDetails.lastName)

        et_email_id.isEnabled = false
        et_email_id.setText(userDetails.email)

        iv_photo.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(view: View?) {
        if (view !=null){
            when(view.id){

                R.id. iv_photo ->{

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED){
                        //showErrorSnackBar("You already have the Storage Permission", false)
                        Constants.showImageChooser(this)
                    }

                    else{
                        //taking permission from the user

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }


            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                //showErrorSnackBar("Storage Permission Granted", false)
                Constants.showImageChooser(this)
            }
            else{
                Toast.makeText(this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG).show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
                if (data!=null){
                    try {
                        val selectedImageUri = data.data!!
                        GlideLoader(this).loadUserPicture(selectedImageUri, iv_photo)

                        //iv_photo.setImageURI(selectedImageUri)
                    }catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@UserProfileActivity,
                        resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }
}