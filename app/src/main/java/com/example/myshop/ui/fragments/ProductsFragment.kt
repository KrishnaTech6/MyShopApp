package com.example.myshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myshop.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {


    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProducts

        textView.text = "This is Products Fragment"

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}