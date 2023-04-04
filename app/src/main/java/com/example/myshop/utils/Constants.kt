package com.example.myshop.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    const val USERS: String = "users"
    const val MYSHOP_PREFERENCES: String = "MyShopPrefs"
    const val LOOGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val PICK_IMAGE_REQUEST_CODE: Int = 1
    const val MALE: String = "male"
    const val FEMALE: String = "female"


    const val FIRSTNAME: String = "firstName"
    const val LASTNAME: String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"


    const val COMPLETE_PROFILE: String = "profileCompleted"


    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val PRODUCT_IMAGE: String = "Product_Image"

    const val PRODUCTS: String = "Products"
    const val USER_ID: String = "user_id"


    const val EXTRA_PRODUCT_ID: String = "extra_product_id"
    const val EXTRA_USER_ID: String = "extra_user_id"
    const val EXTRA_PRODUCT_ID2: String = "extra_product_id2"


    const val PRODUCT_TITLE: String = "productTitle"
    const val PRODUCT_DESC: String = "productDescription"
    const val PRODUCT_PRICE: String = "productPrice"
    const val PRODUCT_QUANTITY: String = "productQuantity"
    const val PRODUCT_IMAGE_EDIT: String = "productImage"



    const val DEFAULT_CART_QUANTITY: String = "1"
    const val CART_QUANTITY: String = "cart_quantity"
    const val CART_ITEMS: String = "cart_items"

    const val PRODUCT_ID: String = "product_id"

    const val ADDRESS: String = "Address"
    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"

    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"

    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121


    fun showImageChooser(activity: Activity){
        //A request for adding image show of phone
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        //launches image selection of phone using constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }// It will tell you the file type (.jpg, jpeg, webp, etc.)
}