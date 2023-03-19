package com.example.myshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Products
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.dashboard_items_rv.view.*

class DashboardItemsAdapter(val context: Context, val items: ArrayList<Products>):
    RecyclerView.Adapter<DashboardItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_items_rv, parent, false)
        return DashboardItemsAdapter.ItemsViewHolder(adapterLayout)

    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {

        val item = items[position]
        if (holder is ItemsViewHolder){
            GlideLoader(context).loadProductPicture(item.productImage, holder.itemView.iv_db_product)
            holder.itemView.tv_db_product_title.text = item.productTitle
            holder.itemView.tv_db_product_price.text = "Rs.${item.productPrice}"
            holder.itemView.tv_db_product_desc.text = item.productDescription
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}