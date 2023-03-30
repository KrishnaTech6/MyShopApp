package com.example.myshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Address
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

    override fun getItemCount(): Int {
        return addressList.size
    }
}