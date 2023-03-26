package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.models.Products
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : BaseActivity(), View.OnClickListener{
    private var mProductId: String = ""
    private lateinit var mProductDatails: Products
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

        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)
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

        mProductDatails = product
        //hideProgressDialog()
        tv_product_name_pd.text = product.productTitle
        tv_product_price_pd.text = "Rs.${product.productPrice}"
        tv_product_desc_pd.text = product.productDescription
        tv_product_quantity_pd.text = product.productQuantity
        GlideLoader(this).loadProductPicture(product.productImage, iv_product_details)

        if (product.productQuantity.toInt() == 0){
            hideProgressDialog()

            btn_add_to_cart.visibility = View.GONE
            tv_product_quantity_pd.text =
                resources.getString(R.string.lbl_out_of_stock)
            tv_product_quantity_pd.setTextColor(
                ContextCompat.getColor(this,R.color.colorSnackBarError )
            )
        }else{
            if (FirestoreClass().getCurrentUserID() == product.user_id){
                hideProgressDialog()
            }else{
                FirestoreClass().checkIfItemExistInCart(this, mProductId)
            }
        }


    }

    private fun addToCart(){
        val cartItems = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDatails.productTitle,
            mProductDatails.productPrice,
            mProductDatails.productImage,
            Constants.DEFAULT_CART_QUANTITY,
            mProductDatails.productQuantity
        )

        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItems)
    }
    fun addToCartSuccess(){
        hideProgressDialog()

        Toast.makeText(this@ProductDetailsActivity,
        resources.getString(R.string.item_added_to_cart),
        Toast.LENGTH_LONG
        ).show()

        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE

    }

    fun productExistsInCart(){
        hideProgressDialog()

        btn_add_to_cart.visibility = View.GONE
        btn_go_to_cart.visibility = View.VISIBLE

    }

    override fun onClick(v: View?) {

        if (v!=null){
            when(v.id){

                R.id.btn_add_to_cart ->{
                    addToCart()
                }

                R.id.btn_go_to_cart ->{
                    val intent =Intent(this@ProductDetailsActivity, MyCartActivity::class.java)
                    startActivity(intent)
                }

            }
        }



    }
}