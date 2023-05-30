package com.example.prm1

import androidx.fragment.app.Fragment

interface Navigable {
    enum class Destination {
        List, Add, Edit, Details, AddPhoto
    }
    fun navigate(to: Destination, fragment: Fragment?)
}