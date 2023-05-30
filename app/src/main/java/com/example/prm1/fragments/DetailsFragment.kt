package com.example.prm1.fragments

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.prm1.Navigable
import com.example.prm1.databinding.FragmentDetailsBinding
import com.example.prm1.model.Product
import java.io.IOException

class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    companion object {
        fun newInstance(product: Product): DetailsFragment {
            return DetailsFragment().apply {
                this.product = product
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.name.text = product.name
        binding.location.text = product.location
        try {
            val inputStream =  requireContext().contentResolver.openInputStream(Uri.parse(product!!.photoUri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            binding.photo.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        binding.btnEdit.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit, EditFragment.newInstance(product))
        }
    }
}
