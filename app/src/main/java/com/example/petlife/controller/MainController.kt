package com.example.petlife.controller

import com.example.petlife.activities.MainActivity
import com.example.petlife.model.pet.Pet
import com.example.petlife.repository.PetDAO
import com.example.petlife.repository.PetSqliteImpl

class MainController(mainActivity: MainActivity) {
    private val petDao:PetDAO = PetSqliteImpl(mainActivity)

    fun insertPet(pet: Pet) = petDao.createPet(pet)
    fun getPet(name: String) = petDao.findPet(name)
    fun getPets() = petDao.findAllPets()
    fun modifyPet(pet: Pet) = petDao.updatePet(pet)
    fun removePet(name: String) = petDao.deletePet(name)
}