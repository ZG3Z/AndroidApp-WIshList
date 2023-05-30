package com.example.prm1.model

import android.graphics.Path

data class PathWithSettings(
    val path: Path = Path(),
    val color: Int,
    val size: Float
)
