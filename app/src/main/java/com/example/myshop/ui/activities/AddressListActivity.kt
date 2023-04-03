package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.ui.adapters.AddressAdapter
import com.example.myshop.utils.Constants
import com.example.myshop.utils.SwipeToDeleteCallBack
import com.example.myshop.utils.SwipeToEditCallBack
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {

    private lateinit var mAddressList: ArrayList<Address>
    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        setUpActionBar()
        supportActionBar?.title=""

        tv_add_addresses.setOnClickListener{
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        if (intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAddress =intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }
        if (mSelectAddress){
            tv_toolbar_address.text = resources.getString(R.string.select_address)
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
            val adapterAddress= AddressAdapter(this@AddressListActivity, addressList, mSelectAddress)
            rv_address_list.adapter = adapterAddress


            if (!mSelectAddress){
                //To ENABLE swipe functionality
                val editSwipeHandler = object : SwipeToEditCallBack(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressAdapter
                        adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }
                val deleteSwipeHandler = object : SwipeToDeleteCallBack(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressAdapter
                        adapter.notifyDeleteItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }
                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)//to edit
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)//to delete
            }


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
    fun successAddressDelete(){
        hideProgressDialog()
        Toast.makeText(this@AddressListActivity,
            "Address was deleted successfully.",
            Toast.LENGTH_SHORT).show()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_address_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_white_arrow_base_24)
        toolbar_address_activity.setNavigationOnClickListener { onBackPressed() }
    }

}