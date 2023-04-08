package com.example.myshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.databinding.FragmentOrdersBinding
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Order
import com.example.myshop.ui.adapters.MyOrdersListAdapter
import kotlinx.android.synthetic.main.fragment_orders.*


class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View ?{

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun populateOrderListinUI(ordersList: ArrayList<Order>){
        hideProgressDialog()

        if (ordersList.size >0){
            rv_orders.visibility = View.VISIBLE
            tv_no_orders_placed.visibility = View.GONE

            rv_orders.layoutManager= LinearLayoutManager(activity)
            rv_orders.setHasFixedSize(true)

            val myOrderAdapter = MyOrdersListAdapter(requireActivity(),ordersList)
            rv_orders.adapter = myOrderAdapter
        }else{
            rv_orders.visibility = View.GONE
            tv_no_orders_placed.visibility = View.VISIBLE
        }

    }

    private fun getMyOrdersList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this@OrdersFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

}