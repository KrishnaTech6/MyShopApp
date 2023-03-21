package com.example.myshop.ui.activities

import android.os.Bundle
import android.view.View
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Products
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity() {
    private var mProductId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setupActionbar()
        supportActionBar?.title= ""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var mUserId: String =""
        if(intent.hasExtra(Constants.EXTRA_USER_ID)){
            mUserId = intent.getStringExtra(Constants.EXTRA_USER_ID)!!
        }
        if (mUserId == FirestoreClass().getCurrentUserID()){
            btn_add_to_cart.visibility = View.GONE
        }else{
            btn_add_to_cart.visibility = View.VISIBLE
        }
        getProductDetails()
    }

    private fun getProductDetails(){
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this, mProductId)
    }
    private fun setupActionbar(){
        setSupportActionBar(toolbar_product_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_product_details.setNavigationOnClickListener { onBackPressed() }
    }

    fun successProductDetails(product: Products){
        hideProgressDialog()
        tv_product_name_pd.text = product.productTitle
        tv_product_price_pd.text = "Rs.${product.productPrice}"
        tv_product_desc_pd.text = product.productDescription
        tv_product_quantity_pd.text = product.productQuantity
        GlideLoader(this).loadProductPicture(product.productImage, iv_product_details)
    }
}