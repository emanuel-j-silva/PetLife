package com.example.petlife.model.event

data class Event(
    val type: EventType,
    val description: String,
    val date: String
)