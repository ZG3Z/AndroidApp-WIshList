package com.example.prm1.fragments

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.prm1.Navigable
import com.example.prm1.R
import com.example.prm1.data.ProductDatabase
import com.example.prm1.data.model.ProductEntity
import com.example.prm1.databinding.FragmentEditBinding
import com.example.prm1.model.Product
import com.example.prm1.model.SettingsNote
import java.io.IOException
import kotlin.concurrent.thread

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var product: Product
    private var mapFragment = MapFragment()
    private var isUserInMap: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater,  container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance(product: Product): EditFragment  {
            return EditFragment().apply {
                this.product = product
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        setHasOptionsMenu(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save -> {
                save()
                true
            }
            R.id.location -> {
                parentFragmentManager.beginTransaction().apply {
                    if(isUserInMap){
                        hide(mapFragment)
                    } else {
                        show(mapFragment)
                    }
                    isUserInMap = !isUserInMap
                    commit()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.beginTransaction().apply {
            add(R.id.container, mapFragment, mapFragment::class.java.name)
            hide(mapFragment)
        }.commit()

        if(product!=null){
            binding.name.setText(product!!.name)
            binding.location.setText(product!!.location)
            try {
                val inputStream =  requireContext().contentResolver.openInputStream(Uri.parse(product!!.photoUri))
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream?.close()
                binding.photo.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun setLocation(location: String) {
        binding.location.text = location
    }

    private fun save() {
        var newProduct: ProductEntity
        if(product.id == -1L){
            ProductEntity(
                name = binding.name.text.toString(),
                location = binding.location.text.toString(),
                photoUri = product.photoUri
            ).also { newProduct = it }
            thread {
                ProductDatabase.open(requireContext()).products.addProduct(newProduct)
                (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
            }
        } else {
            thread {
                newProduct = ProductDatabase.open(requireContext()).products.getProduct(product.id)
                newProduct.name = binding.name.text.toString()
                newProduct.location = binding.location.text.toString()
                ProductDatabase.open(requireContext()).products.addProduct(newProduct)
                (activity as? Navigable)?.navigate(Navigable.Destination.List, null)
            }
        }
    }
}
