package com.example.petlife.model.pet

import android.os.Parcelable
import com.example.petlife.model.event.Event
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pet(
    val name: String = "",
    val birthDate: String = "",
    val type: PetType = PetType.DOG,
    val color: String = "",
    val size: Size = Size.MEDIUM,
    var events: List<Event> = emptyList()
):Parcelable
