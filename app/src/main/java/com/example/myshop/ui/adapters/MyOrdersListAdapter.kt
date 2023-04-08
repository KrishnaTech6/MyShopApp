package com.example.myshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Order
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.item_orderlist_layout.view.*

class MyOrdersListAdapter(val context: Context,
                          val ordersList: ArrayList<Order>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_orderlist_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = ordersList[position]

        if (holder is ViewHolder){
            holder.itemView.tv_title_orders.text = model.title
            holder.itemView.tv_product_price_orders.text = "Rs.${model.total_amount}"
            GlideLoader(context)
                .loadProductPicture(
                    model.image,
                    holder.itemView.iv_item_orders
                )

            holder.itemView.ib_delete_item.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return ordersList.size
    }
}