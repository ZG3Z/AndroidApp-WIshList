package com.example.prm1.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.prm1.Navigable
import com.example.prm1.databinding.ListItemBinding
import com.example.prm1.fragments.DetailsFragment
import com.example.prm1.fragments.ListFragment
import com.example.prm1.model.Product
import java.io.IOException

class ProductViewHolder(private val context: Context, private val binding: ListItemBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bind(product: Product){
        binding.name.text = product.name
        binding.location.text = product.location
        try {
            val inputStream =  context.contentResolver.openInputStream(Uri.parse(product.photoUri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            binding.photo.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

class ProductsAdapter : RecyclerView.Adapter<ProductViewHolder>() {
    private val data = mutableListOf<Product>()
    private var listener: RemoveItemListener? = null

    interface RemoveItemListener { fun onDataSetChanged(product: Product) }

    fun setListener(listener: ListFragment) { this.listener = listener }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(parent.context, binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            (holder.itemView.context as AppCompatActivity as? Navigable)?.navigate(Navigable.Destination.Details, DetailsFragment.newInstance(data[position]))
        }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Are you sure you want to delete this product?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    listener?.onDataSetChanged(data[position])
                    data.removeAt(position)
                    notifyDataSetChanged()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }
    }

    fun replace(newData: List<Product>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}