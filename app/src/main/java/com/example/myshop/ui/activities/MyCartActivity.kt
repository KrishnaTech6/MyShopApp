package com.example.myshop.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.ui.adapters.MyCartListAdapter
import kotlinx.android.synthetic.main.activity_cart.*

class MyCartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setUpActionBar()
        supportActionBar?.title= ""

        getCartProductList()


    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_cart_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)

        toolbar_cart_activity.setNavigationOnClickListener { onBackPressed() }
    }


    fun getCartProductList(){
        showDialogProgress(resources.getString(R.string.please_wait))

        FirestoreClass().getCartItemFromFirestore(this)
    }

    fun successCartItemFromFirestore(cartItemList: ArrayList<CartItem>){
        hideProgressDialog()

        if (cartItemList.size > 0){
            rv_cart_items.visibility = View.VISIBLE
            tv_cart_is_empty.visibility  = View.GONE
            rv_cart_items.layoutManager = LinearLayoutManager(this) //didnt understand
            rv_cart_items.setHasFixedSize(true)
            val adapterProducts = MyCartListAdapter(this, cartItemList)
            rv_cart_items.adapter = adapterProducts
        }
        else{
            rv_cart_items.visibility = View.GONE
            tv_cart_is_empty.visibility  = View.VISIBLE
        }

    }
}