package com.example.prm1

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.prm1.databinding.ActivityMainBinding
import com.example.prm1.fragments.*

class MainActivity : AppCompatActivity(), Navigable{

    private lateinit var listFragment: ListFragment
   // private lateinit var mapFragment: MapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setLogo(R.drawable.logo)
            setDisplayUseLogoEnabled(true)
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@MainActivity, R.color.black)))
        }

        listFragment = ListFragment()
       // mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    override fun navigate(to: Navigable.Destination, fragment: Fragment?) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.List -> replace(
                    R.id.container,
                    listFragment,
                    listFragment.javaClass.name
                )
                Navigable.Destination.Add -> {
                    replace(R.id.container, fragment!!, EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Edit -> {
                    replace(R.id.container, fragment!!, EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
                Navigable.Destination.Details -> {
                    replace(R.id.container, fragment!!, DetailsFragment::class.java.name)
                    addToBackStack(null)
                }
                Navigable.Destination.AddPhoto -> {
                    replace(R.id.container, fragment!!, PhotoFragment::class.java.name)
                }
                /*
                Navigable.Destination.Map -> {
                    replace(R.id.container, mapFragment, mapFragment::class.java.name)
                }

                 */
            }.commit()
        }
    }
}