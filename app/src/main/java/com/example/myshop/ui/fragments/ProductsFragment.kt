package com.example.myshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.R
import com.example.myshop.databinding.FragmentProductsBinding
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Products
import com.example.myshop.ui.activities.AddProductActivity
import com.example.myshop.ui.adapters.MyProductsAdapter
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {


    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    fun successProductsListFromFirestore(productsList: ArrayList<Products>){
        hideProgressDialog()

        if (productsList.size > 0){
            rv_recyclerview_products.visibility = View.VISIBLE
            tv_no_products_found.visibility  = View.GONE
            rv_recyclerview_products.layoutManager = LinearLayoutManager(activity) //didnt understand
            rv_recyclerview_products.setHasFixedSize(true)
            val adapterProducts = MyProductsAdapter(requireActivity(), productsList)
            rv_recyclerview_products.adapter = adapterProducts
        }
        else{
            rv_recyclerview_products.visibility = View.GONE
            tv_no_products_found.visibility  = View.VISIBLE
        }

    }
    private fun getProductListFromFirestore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_products_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product ->{
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}