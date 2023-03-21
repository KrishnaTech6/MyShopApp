package com.example.myshop.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Products
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_edit_product_data.*
import java.io.IOException

class EditProductDataActivity : BaseActivity() {

    private var mProductId: String = ""
    private var mProductImageUploadURL: String = ""
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mProduct: Products

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product_data)

        setUpActionBar()
        supportActionBar?.title=""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID2)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID2)!!

            Log.i("Product Id",mProductId)
        }

        getProductDetails()

        iv_edit_product_image.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

                Constants.showImageChooser(this@EditProductDataActivity)
            }
            else{
                ActivityCompat.requestPermissions(this@EditProductDataActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE)
            }
        }

        btn_submit_product_edit.setOnClickListener{

            if (mSelectedImageFileUri!=null){
                uploadProductImage()
            }
            else{
                editProductData()
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

                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this@EditProductDataActivity).loadUserPicture(mSelectedImageFileUri!!, iv_edit_product_image)

                    }catch (e: IOException){
                        e.printStackTrace()

                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.PRODUCT_IMAGE)
    }
    fun imageUploadSuccess(imageUrl: String){
        hideProgressDialog()
        mProductImageUploadURL = imageUrl
        editProductData()
    }

    private fun getProductDetails(){
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductId)
    }


    private fun setUpActionBar(){
        setSupportActionBar(toolbar_edit_product)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_edit_product.setNavigationOnClickListener { onBackPressed() }
    }
    fun successProductDetails(product: Products){
        mProduct= product
        hideProgressDialog()
        et_edit_product_title.setText(product.productTitle)
        et_edit_product_price.setText(product.productPrice)
        et_edit_product_desc.setText(product.productDescription)
        et_edit_product_quantity.setText(product.productQuantity)
        GlideLoader(this).loadProductPicture(product.productImage, iv_edit_product_image)
    }

    private fun editProductData(){
        val productHashMap= HashMap<String, Any>()

        val productName = et_edit_product_title.text.toString().trim{it <=' '}
        if (productName!= mProduct.productTitle){
            productHashMap[Constants.PRODUCT_TITLE] = productName
        }
        val productPrice = et_edit_product_price.text.toString().trim{it <=' '}
        if (productPrice!= mProduct.productPrice){
            productHashMap[Constants.PRODUCT_PRICE] = productPrice
        }
        val productDesc = et_edit_product_desc.text.toString().trim{it <=' '}
        if (productDesc!= mProduct.productDescription){
            productHashMap[Constants.PRODUCT_DESC] = productDesc
        }
        val productQuantity = et_edit_product_quantity.text.toString().trim{it <=' '}
        if (productQuantity!= mProduct.productQuantity){
            productHashMap[Constants.PRODUCT_QUANTITY] = productQuantity
        }
        if (mProductImageUploadURL.isNotEmpty()){
            productHashMap[Constants.PRODUCT_IMAGE_EDIT] = mProductImageUploadURL
        }

        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().editProductData(this, mProductId, productHashMap )
    }

    fun editProductDataSuccess(){

        hideProgressDialog()

        Toast.makeText(this,
            resources.getString(R.string.product_data_update_success),
            Toast.LENGTH_LONG).show()
        finish()
    }


}