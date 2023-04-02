package com.example.myshop.ui.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.ui.activities.AddEditAddressActivity
import com.example.myshop.ui.activities.AddressListActivity
import com.example.myshop.utils.Constants
import kotlinx.android.synthetic.main.address_item.view.*

class AddressAdapter( private val context: Context, private val addressList: ArrayList<Address>): RecyclerView.Adapter<AddressAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_item, parent, false)
        return AddressAdapter.ViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = addressList[position]

        holder.itemView.tv_address_full_name.text = item.name
        holder.itemView.tv_address_item.text = item.address
        holder.itemView.tv_address_phone_number.text = item.mobileNumber
        holder.itemView.tv_address_type.text = item.type

    }
    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, addressList[position])
        activity.startActivity(intent)
        notifyItemChanged(position)
    }
    fun notifyDeleteItem(activity: AddressListActivity, position: Int){

            val builder= AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Are you sure you want to delete this Address?")
            builder.setIcon(android.R.drawable.ic_dialog_alert)
            builder.setPositiveButton("Yes"){ dialogInterface, _ ->
                //delete the address
                activity.showDialogProgress(context.resources.getString(R.string.please_wait))
                FirestoreClass().deleteAddress(activity, addressList[position].id)
                notifyItemChanged(position)
                FirestoreClass().getAddressList(activity)
                dialogInterface.dismiss()
            }
            builder.setNegativeButton("No"){ dialogInterface, _ ->
                notifyItemChanged(position)
                dialogInterface.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
    }

    override fun getItemCount(): Int {
        return addressList.size
    }
}