package com.example.prm1.fragments

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.prm1.Navigable
import com.example.prm1.R
import com.example.prm1.databinding.FragmentPhotoBinding
import com.example.prm1.model.Product
import com.example.prm1.model.SettingsNote

class PhotoFragment : Fragment() {

    private lateinit var binding: FragmentPhotoBinding
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var settingsPhotoFragment = SettingsPhotoFragment()
    private var isUserInSettings: Boolean = false
    private var imageUri: Uri? = null
    private val onTakePhoto: (Boolean) -> Unit = { photography: Boolean ->
        if(photography) {
            loadBitmap()
        } else {
            imageUri?.let {
                requireContext().contentResolver.delete(it, null, null)
            }
        }
    }

    private fun loadBitmap() {
        val imageUri = imageUri?: return
        requireContext().contentResolver.openInputStream(imageUri)?.use {
            BitmapFactory.decodeStream(it)
        }?.let {
            binding.photo.background = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture(),
            onTakePhoto
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentPhotoBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parentFragmentManager.beginTransaction().apply {
            add(R.id.container, settingsPhotoFragment, settingsPhotoFragment::class.java.name)
            hide(settingsPhotoFragment)
        }.commit()

        createPicture()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_photo, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.settings -> {
                parentFragmentManager.beginTransaction().apply {
                    if(isUserInSettings){
                        hide(settingsPhotoFragment)
                    } else {
                        show(settingsPhotoFragment)
                    }
                    isUserInSettings = !isUserInSettings
                    commit()
                }
                true
            }
            R.id.save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        setHasOptionsMenu(false)
    }


    private fun createPicture(){
        val imagesUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"photoGRAPH.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        cameraLauncher.launch(imageUri)
    }

    fun setSettings(settings: SettingsNote) {
        binding.photo.apply {
            binding.photo.note = settings
        }
    }

    private fun save() {
        val bmp = binding.photo.generateBitmap()
        imageUri?.let {
            requireContext().contentResolver.openOutputStream(it)?.use {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, it)
            }
        }

        (activity as? Navigable)?.navigate(Navigable.Destination.Add, EditFragment.newInstance(
            Product(name="", location= "", photoUri = imageUri.toString())
        ))
    }
}