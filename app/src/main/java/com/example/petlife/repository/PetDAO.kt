package com.example.petlife.repository

import com.example.petlife.model.pet.Pet

interface PetDAO {
    fun createPet(pet: Pet): Long
    fun findPet(name: String): Pet
    fun findAllPets(): MutableList<Pet>
    fun updatePet(pet: Pet): Int
    fun deletePet(name: String): Int
}