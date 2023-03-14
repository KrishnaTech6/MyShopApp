package com.example.myshop.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class UserProfileActivity : BaseActivity(), View.OnClickListener{

    private var mUserDetails: User = User()   //putting it equal to User() set default value if not putExtra
    private var mSelectedImageFileUri : Uri? = null
    private var mUploadedImageFileURL : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)



        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name.isEnabled = false
        et_first_name.setText(mUserDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(mUserDetails.lastName)

        et_email_id.isEnabled = false
        et_email_id.setText(mUserDetails.email)

        iv_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit_user.setOnClickListener(this@UserProfileActivity)
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

                R.id.btn_submit_user ->{
                    if (validateUserProfileDetails()){
                        showDialogProgress(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri !=null){
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        }
                        else{
                           updateUserProfileDetails()
                        }
                    }
                }
            }
        }

    }

    private fun updateUserProfileDetails(){
        val userHashMap= HashMap<String, Any>()

        val mobileNo = et_mobile.text.toString().trim{it <=' '}

        val gender = if (rb_male.isChecked)
            Constants.MALE
        else Constants.FEMALE

        if (mobileNo.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNo.toLong()
        }
        if (mUploadedImageFileURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUploadedImageFileURL
        }

        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1


        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()

        Toast.makeText(this@UserProfileActivity,
        resources.getString(R.string.msg_profile_update_success),
        Toast.LENGTH_LONG).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
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
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_photo)

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

    private fun validateUserProfileDetails(): Boolean{
        return when{
            TextUtils.isEmpty(et_mobile.text.toString().trim{it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_no), true)
                false
            }
            else -> {
                true
            }
        }

    }

    fun imageUploadSuccess(imageUrl: String){
        mUploadedImageFileURL = imageUrl
        updateUserProfileDetails()

    }


}