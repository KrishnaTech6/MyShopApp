package com.example.myshop.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {

    const val USERS: String = "users"
    const val MYSHOP_PREFERENCES: String = "MyShopPrefs"
    const val LOOGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val PICK_IMAGE_REQUEST_CODE: Int = 1

    fun showImageChooser(activity: Activity){
        //A request for adding image show of phone
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        //launches image selection of phone using constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}