package com.example.petlife

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val name: String,
    val birthDate: String,
    val type: Type,
    val color: String,
    val size: Size,
) : Parcelable
