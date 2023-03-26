package com.example.myshop.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.models.Products
import com.example.myshop.ui.adapters.MyCartListAdapter
import kotlinx.android.synthetic.main.activity_cart.*

class MyCartActivity : BaseActivity() {

    private lateinit var mProductsList: ArrayList<Products>
    private lateinit var mCartListItem: ArrayList<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setUpActionBar()
        supportActionBar?.title= ""




    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_cart_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_cart_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        //getCartProductList()
        getAllProducts()
    }


    fun getCartProductList(){
        //showDialogProgress(resources.getString(R.string.please_wait))

        FirestoreClass().getCartItemFromFirestore(this)
    }

    fun successAllItemsListFromFirestore(productsList: ArrayList<Products>){
        hideProgressDialog()
        mProductsList = productsList
        getCartProductList()
    }

    private fun getAllProducts(){  //Using this to get stock quantity
        showDialogProgress(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProducts(this)
    }

    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartProductList()
    }

    fun successRemoveCartItem(){
        hideProgressDialog()
        Toast.makeText(this,
            resources.getString(R.string.item_remove_success),
            Toast.LENGTH_SHORT).show()

        getCartProductList()
    }

    fun successCartItemFromFirestore(cartItemList: ArrayList<CartItem>){
        hideProgressDialog()

        for (product in mProductsList){
            for (cart in cartItemList){
                if (product.product_id == cart.product_id){
                    cart.stock_quantity = product.productQuantity
                    if (product.productQuantity.toInt()==0){
                        cart.cart_quantity = product.productQuantity
                    }

                }
            }
        }
        mCartListItem = cartItemList

        if (mCartListItem.size > 0){
            rv_cart_items.visibility = View.VISIBLE

            tv_cart_is_empty.visibility  = View.GONE
            rv_cart_items.layoutManager = LinearLayoutManager(this@MyCartActivity) //didnt understand
            rv_cart_items.setHasFixedSize(true)
            val adapterProducts = MyCartListAdapter(this@MyCartActivity, cartItemList)
            rv_cart_items.adapter = adapterProducts

            var subTotal: Double =0.0

            for (item in mCartListItem){

                val availableQuantity = item.stock_quantity.toInt()

                if (availableQuantity>0){
                    val price= item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)

                }

            }

            tv_subtotal_price.text = "Rs.${ subTotal }"
            shipping_price.text = "Rs.40" //TODo change logic for shipping price

            if (subTotal>0){
                ll_checkout.visibility = View.VISIBLE

                val total:String = "Rs.${ subTotal + 40 }"  //TODo change logic for shipping price
                tv_total_price.text = total
            }
            else{
                ll_checkout.visibility = View.VISIBLE
            }
        }
        else{
            ll_checkout.visibility = View.GONE
            rv_cart_items.visibility = View.GONE
            tv_cart_is_empty.visibility  = View.VISIBLE
        }

    }
}