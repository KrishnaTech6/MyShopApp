package com.example.myshop.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Products
import com.example.myshop.ui.activities.EditProductDataActivity
import com.example.myshop.ui.activities.ProductDetailsActivity
import com.example.myshop.ui.fragments.ProductsFragment
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import kotlinx.android.synthetic.main.product_item.view.*

class MyProductsAdapter(private val context: Context,
                        private var products: ArrayList<Products>,
                        private val fragment: ProductsFragment):
    RecyclerView.Adapter<MyProductsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val view:View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = products[position]
        if (holder is ItemViewHolder){

            GlideLoader(context).loadProductPicture(item.productImage, holder.view.iv_item_product)
            holder.view.tv_product_title.text = item.productTitle

            if (holder.view.tv_product_price.text.contains("Rs.")){

                holder.view.tv_product_price.text = item.productPrice
            }else{
                holder.view.tv_product_price.text = "Rs.${item.productPrice}"
            }


            holder.view.ib_delete_item.setOnClickListener{
                fragment.showAlertDialogToDeleteProducts(item.product_id)

            }
            holder.view.ib_edit_item.setOnClickListener{

                val intent = Intent(context, EditProductDataActivity::class.java )
                intent.putExtra(Constants.EXTRA_PRODUCT_ID2, item.product_id)
                context.startActivity(intent)

            }

            holder.view.setOnClickListener{
                val intent = Intent(context, ProductDetailsActivity::class.java )
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, item.product_id)
                intent.putExtra(Constants.EXTRA_USER_ID, item.user_id)
                context.startActivity(intent)
            }

        }


    }

    override fun getItemCount(): Int {
        return products.size
    }


}
