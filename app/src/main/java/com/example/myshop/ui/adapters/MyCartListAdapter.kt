package com.example.myshop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.CartItem
import com.example.myshop.ui.activities.ProductDetailsActivity
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.cart_item.view.*


class MyCartListAdapter(private val context: Context, private val cartItem: ArrayList<CartItem>):
    RecyclerView.Adapter<MyCartListAdapter.CartViewHolder>()  {

    class CartViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItem[position]

        GlideLoader(context).loadProductPicture(item.image, holder.view.iv_item)
        holder.view.tv_title.text = item.title
        holder.view.tv_price.text = "Rs.${ item.price }"
        holder.view.tv_cart_quantity.text = item.cart_quantity

        if (item.cart_quantity=="0"){
            holder.view.ib_add_item.visibility  = View.GONE
            holder.view.ib_remove_item.visibility  = View.GONE

            holder.view.tv_cart_quantity.text =
                context.getText(R.string.lbl_out_of_stock)

            holder.view.tv_cart_quantity.setTextColor(
                    ContextCompat.getColor(context,
                        R.color.colorSnackBarError)
            )
        }else{
            holder.view.ib_add_item.visibility  = View.VISIBLE
            holder.view.ib_remove_item.visibility  = View.VISIBLE
            holder.view.tv_cart_quantity.setTextColor(
                ContextCompat.getColor(context,
                    R.color.colorSecondaryText)
            )
        }

        holder.view.ib_delete_item.setOnClickListener{
            //TODO: Method to delete cart item
        }

        holder.view.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, item.product_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return cartItem.size
    }

}