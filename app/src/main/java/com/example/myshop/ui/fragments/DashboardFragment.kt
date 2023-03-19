package com.example.myshop.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myshop.R
import com.example.myshop.databinding.FragmentDashboardBinding
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Products
import com.example.myshop.ui.activities.SettingsActivity
import com.example.myshop.ui.adapters.DashboardItemsAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItems()
    }

    fun successItemsListFromFirestore(itemsList: ArrayList<Products>){
        hideProgressDialog()

        if (itemsList.size>0){
            rv_dashboard_items.visibility = View.VISIBLE
            tv_dashboard_item_not_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter= DashboardItemsAdapter(requireActivity(), itemsList)
            rv_dashboard_items.adapter = adapter


        }
        else{
            rv_dashboard_items.visibility = View.GONE
            tv_dashboard_item_not_found.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItems(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboarItemsList(this@DashboardFragment)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
       // val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.settings_action ->{
                startActivity(Intent(activity, SettingsActivity::class.java))
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