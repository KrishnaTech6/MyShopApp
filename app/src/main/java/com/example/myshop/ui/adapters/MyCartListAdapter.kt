package com.example.myshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.CartItem
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.cart_item.view.*


class MyCartListAdapter(private val context: Context, private val cartItem: ArrayList<CartItem>):
    RecyclerView.Adapter<MyCartListAdapter.CartViewHolder>()  {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return MyCartListAdapter.CartViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItem[position]
        GlideLoader(context).loadProductPicture(item.image,holder.itemView.iv_item )
        holder.itemView.tv_title.text = item.title
        holder.itemView.tv_price.text = item.title

    }

    override fun getItemCount(): Int {
        return cartItem.size
    }
}