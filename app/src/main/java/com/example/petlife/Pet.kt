package com.example.petlife

data class Pet(
    val name: String,
    val birthDate: String,
    val type: Type,
    val color: String,
    val size: Size,
    val lastVetVisit: String,
    val lastPetShopVisit: String,
    val lastVaccination: String,
)
