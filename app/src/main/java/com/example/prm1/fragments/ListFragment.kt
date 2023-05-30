package com.example.prm1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm1.adapters.ProductsAdapter
import com.example.prm1.Navigable
import com.example.prm1.data.ProductDatabase
import com.example.prm1.databinding.FragmentListBinding
import com.example.prm1.model.Product
import kotlin.concurrent.thread

class ListFragment : Fragment(), ProductsAdapter.RemoveItemListener {

    private lateinit var binding: FragmentListBinding
    private var adapter: ProductsAdapter? = null
    private lateinit var db: ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater,  container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductsAdapter()
        loadData()

        binding.list.let {
            it.adapter = adapter
            adapter!!.setListener(this)
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.AddPhoto, PhotoFragment())
        }

        /*
        binding.button.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Map, null)
        }

         */
    }

    private fun loadData() =  thread {
        val products = db.products.getAll().map { entity ->
            Product(entity.id, entity.name, entity.location, entity.photoUri)
        }

        requireActivity().runOnUiThread {
            adapter?.replace(products)
        }
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    override fun onDataSetChanged(product: Product) {
        thread{
            db.products.deleteProduct(product.id)
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}