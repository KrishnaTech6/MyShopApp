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
import com.example.myshop.models.Products
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private var mSelectedImageFileUri: Uri? =null
    private var mProductImageUploadURL: String =""
    private lateinit var products: Products
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionbar()
        supportActionBar?.title= ""

        iv_product_image.setOnClickListener(this)
        btn_submit_product.setOnClickListener(this)

    }

    private fun setupActionbar(){
        setSupportActionBar(toolbar_add_product)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_add_product.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {

        if (v!=null ){
            when (v.id){
                R.id.iv_product_image ->{
                    if(ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED){

                        Constants.showImageChooser(this@AddProductActivity)
                    }
                    else{
                        ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btn_submit_product ->{
                    if (validateAddProductDetails()){
                        uploadProductImage()
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
            if (grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
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
                        iv_edit_product_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))
                         mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, iv_product_image)

                        //iv_photo.setImageURI(selectedImageUri)
                    }catch (e: IOException){
                        e.printStackTrace()

                    }
                }
            }
        }
    }

    private fun validateAddProductDetails():Boolean{
        return when
        {
            TextUtils.isEmpty(et_product_title.text.toString().trim{it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title),true)
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim{it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price),true)
                false
            }
            TextUtils.isEmpty(et_product_desc.text.toString().trim{it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_desc),true)
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim{it <=' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity),true)
                false
            }
            (mSelectedImageFileUri ==null) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image),true)
                false
            }
            else ->{
                return true
            }

        }

    }
    fun uploadProductImage(){
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.PRODUCT_IMAGE)
    }
    fun imageUploadSuccess(imageUrl: String){
        mProductImageUploadURL = imageUrl
//        hideProgressDialog()
//        showErrorSnackBar("Image Uploaded $imageUrl", false)
        uploadProductData()
    }
    fun productUploadSuccess(){
        hideProgressDialog()

        Toast.makeText(
            this@AddProductActivity,
            resources.getString(R.string.product_uploaded_successfully),
            Toast.LENGTH_LONG)
            .show()
    }

    private fun uploadProductData(){
        products = Products(
             FirestoreClass().getCurrentUserID() + {et_product_title.text.toString().trim{it<= ' '}},
            et_product_title.text.toString().trim{it<= ' '},
            et_product_price.text.toString().trim{it<=' '},
            et_product_desc.text.toString().trim{it<= ' '},
            et_product_quantity.text.toString().trim{it<=' '},
            mProductImageUploadURL
        )
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().uploadProductData(this, products)
    }
}