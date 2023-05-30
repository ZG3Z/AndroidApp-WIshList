package com.example.prm1.fragments

import android.graphics.Color
import android.os.Bundle
import android.provider.ContactsContract.Contacts.Photo
import android.view.*
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.prm1.R
import com.example.prm1.databinding.FragmentSettingsPhotoBinding
import com.example.prm1.model.SettingsNote

class SettingsPhotoFragment: Fragment(), SeekBar.OnSeekBarChangeListener {
    private lateinit var binding: FragmentSettingsPhotoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSettingsPhotoBinding.inflate(
            inflater, container, false
        ).also {
            binding = it
        }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.seekBar.setOnSeekBarChangeListener(this)
        binding.color.setOnCheckedChangeListener{ _, _ -> changeSettings()}
        binding.noteText.addTextChangedListener{changeSettings()}
    }


    private fun idToColor(id: Int) = when (id){
        R.id.black -> Color.BLACK
        R.id.blue -> Color.BLUE
        R.id.red -> Color.RED
        R.id.green -> Color.GREEN
        else -> Color.BLACK
    }


    override fun onProgressChanged(seekBar: SeekBar?, value: Int, isUserSet: Boolean) {
        if(isUserSet) changeSettings()
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}

    private fun changeSettings(){
        val settings = SettingsNote(
            binding.noteText.text.toString(),
            binding.seekBar.progress.toFloat(),
            idToColor(binding.color.checkedRadioButtonId)
        )

        (parentFragmentManager.findFragmentByTag(PhotoFragment::class.java.name) as? PhotoFragment)?.let {
            it.setSettings(settings)
        }
    }
}