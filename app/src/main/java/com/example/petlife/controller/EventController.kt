package com.example.petlife.controller

import com.example.petlife.activities.ListEventsActivity
import com.example.petlife.model.event.Event
import com.example.petlife.repository.PetDAO
import com.example.petlife.repository.PetSqliteImpl

class EventController(listEventsActivity: ListEventsActivity) {
    private val petDao: PetDAO = PetSqliteImpl(listEventsActivity)

    fun insertEvent(petName: String, event: Event) = petDao.addEvent(petName, event)
    fun findEventsByPet(petName: String) = petDao.findEventsByPet(petName)
    fun updateEvent(event: Event) = petDao.updateEvent(event)
    fun deleteEvent(eventId: Long) = petDao.deleteEvent(eventId)
}