package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.ui.adapters.AddressAdapter
import com.example.myshop.utils.SwipeToEditCallBack
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {

    private lateinit var mAddressList: ArrayList<Address>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        setUpActionBar()
        supportActionBar?.title=""

        tv_add_addresses.setOnClickListener{
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        getAddressFromFirestore()
    }

    fun successAddressFromFirestore(addressList : ArrayList<Address>){
        hideProgressDialog()

        mAddressList = addressList

        if (mAddressList.size > 0){
            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility  = View.GONE

            rv_address_list.layoutManager = LinearLayoutManager(this@AddressListActivity)
            rv_address_list.setHasFixedSize(true)
            val adapterAddress= AddressAdapter(this@AddressListActivity, addressList)
            rv_address_list.adapter = adapterAddress


            //To ENABLE swipe functionality
            val editSwipeHandler = object : SwipeToEditCallBack(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = rv_address_list.adapter as AddressAdapter
                    adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                }
            }
            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(rv_address_list)

        }
        else{
            rv_address_list.visibility = View.GONE
            tv_no_address_found.visibility  = View.VISIBLE
        }

    }

    private fun getAddressFromFirestore(){
        showDialogProgress(resources.getString(R.string.please_wait))

        FirestoreClass().getAddressList(this@AddressListActivity)
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_address_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        toolbar_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

}