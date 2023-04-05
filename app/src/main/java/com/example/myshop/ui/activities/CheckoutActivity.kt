package com.example.myshop.ui.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.models.CartItem
import com.example.myshop.models.Products
import com.example.myshop.ui.adapters.MyCartListAdapter
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductsList: ArrayList<Products>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        setupToolbar()
        supportActionBar?.title = ""

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if(mAddressDetails!=null){
            tv_checkout_address_type.text = mAddressDetails?.type
            tv_checkout_full_name.text = mAddressDetails?.name
            tv_checkout_address.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.pinCode}"
            tv_checkout_additional_note.text = mAddressDetails?.additionalNote
            tv_checkout_mobile_number.text = mAddressDetails?.mobileNumber

            if (mAddressDetails?.otherDetails!!.isNotEmpty()){
                tv_checkout_other_details.text = mAddressDetails?.otherDetails
            }
        }
        getProductList()
    }

    fun getProductList(){
        showDialogProgress(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProducts(this@CheckoutActivity)

    }
    fun successProductsList(productsList: ArrayList<Products>){
        mProductsList=productsList
        hideProgressDialog()
        getCartItemList()
    }

    fun successCartItemsList(cartItemsList: ArrayList<CartItem>){
        hideProgressDialog()
        mCartItemsList= cartItemsList

        for (product in mProductsList){
            for (cartitem in mCartItemsList){
                if (product.product_id== cartitem.product_id){
                    cartitem.stock_quantity = product.productQuantity
                }
            }
        }


        rv_cart_list_items.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        rv_cart_list_items.setHasFixedSize(true)

        val checkoutAdapter = MyCartListAdapter(this ,mCartItemsList, false)
        rv_cart_list_items.adapter = checkoutAdapter

        var subTotal: Double =0.0

        for (item in mCartItemsList){

            val availableQuantity = item.stock_quantity.toInt()

            if (availableQuantity>0){
                val price= item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)

            }

        }

        tv_checkout_sub_total.text = "Rs.$subTotal"
        val shippingPrice = 40
        tv_checkout_shipping_charge.text = "Rs. $shippingPrice"

        val totalAmount = subTotal+shippingPrice

        tv_checkout_total_amount.text = "Rs.$totalAmount"


    }

    private fun getCartItemList(){
        FirestoreClass().getCartItemFromFirestore(this@CheckoutActivity)
    }
    private fun setupToolbar(){
        setSupportActionBar(toolbar_checkout_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        toolbar_checkout_activity.setNavigationOnClickListener { onBackPressed() }

    }
}