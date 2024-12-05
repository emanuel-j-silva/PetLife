package com.example.petlife.model.pet

data class Pet(
    val name: String,
    val birthDate: String,
    val type: PetType,
    val color: String,
    val size: Size,
)
