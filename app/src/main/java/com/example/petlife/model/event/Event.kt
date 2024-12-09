package com.example.petlife.model.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: Long = 0L,
    val type: EventType,
    val description: String,
    val date: String
):Parcelable