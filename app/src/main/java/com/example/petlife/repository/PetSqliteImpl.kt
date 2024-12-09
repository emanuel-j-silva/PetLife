package com.example.petlife.repository

import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.petlife.R
import com.example.petlife.model.pet.Pet
import com.example.petlife.model.pet.PetType
import com.example.petlife.model.pet.Size
import com.example.petlife.model.event.Event
import com.example.petlife.model.event.EventType

class PetSqliteImpl(context: Context): PetDAO {

    companion object {
        private const val PET_DATABASE_FILE = "pet"
        private const val PET_TABLE = "pet"
        private const val EVENT_TABLE = "event"

        private const val NAME_COLUMN = "name"
        private const val BIRTH_DATE_COLUMN = "birth_date"
        private const val TYPE_COLUMN = "type"
        private const val COLOR_COLUMN = "color"
        private const val SIZE_COLUMN = "size"

        private const val EVENT_ID_COLUMN = "id"
        private const val EVENT_TYPE_COLUMN = "event_type"
        private const val EVENT_DESCRIPTION_COLUMN = "description"
        private const val EVENT_DATE_COLUMN = "date"
        private const val EVENT_TIME_COLUMN = "time"
        private const val PET_NAME_COLUMN = "pet_name"

        private const val CREATE_PET_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PET_TABLE (" +
                    "$NAME_COLUMN TEXT NOT NULL PRIMARY KEY," +
                    "$BIRTH_DATE_COLUMN TEXT NOT NULL," +
                    "$TYPE_COLUMN TEXT NOT NULL," +
                    "$COLOR_COLUMN TEXT NOT NULL," +
                    "$SIZE_COLUMN TEXT NOT NULL);"

        private const val CREATE_EVENT_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $EVENT_TABLE (" +
                    "$EVENT_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$EVENT_TYPE_COLUMN TEXT NOT NULL," +
                    "$EVENT_DESCRIPTION_COLUMN TEXT NOT NULL," +
                    "$EVENT_DATE_COLUMN TEXT NOT NULL," +
                    "$EVENT_TIME_COLUMN TEXT," +
                    "$PET_NAME_COLUMN TEXT NOT NULL," +
                    "FOREIGN KEY($PET_NAME_COLUMN) REFERENCES $PET_TABLE($NAME_COLUMN) " +
                    "ON DELETE CASCADE);"
    }

    private val petDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        PET_DATABASE_FILE, MODE_PRIVATE, null)

    init {
        try {
            petDatabase.execSQL(CREATE_PET_TABLE_STATEMENT)
            petDatabase.execSQL(CREATE_EVENT_TABLE_STATEMENT)
        } catch (se: SQLException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createPet(pet: Pet) =
        petDatabase.insert(PET_TABLE, null, petToContentValue(pet))

    override fun findPet(name: String): Pet {
        val cursor = petDatabase.query(
            true, PET_TABLE, null, "$NAME_COLUMN = ?", arrayOf(name),
            null, null, null, null
        )
        return if (cursor.moveToFirst()) {
            cursorToPet(cursor).apply {
                events = findEventsByPet(name)
            }
        } else {
            Pet()
        }
    }

    override fun findAllPets(): MutableList<Pet> {
        val petList = mutableListOf<Pet>()

        val cursor = petDatabase.rawQuery("SELECT * FROM $PET_TABLE", null)
        while (cursor.moveToNext()) {
            val pet = cursorToPet(cursor)
            pet.events = findEventsByPet(pet.name)
            petList.add(pet)
        }

        return petList
    }

    override fun updatePet(pet: Pet) = petDatabase.update(
        PET_TABLE, petToContentValue(pet), "$NAME_COLUMN = ?", arrayOf(pet.name)
    )

    override fun deletePet(name: String) = petDatabase.delete(
        PET_TABLE,
        "$NAME_COLUMN = ?",
        arrayOf(name)
    )

    override fun addEvent(petName: String, event: Event): Long {
        return ContentValues().apply {
            put(EVENT_TYPE_COLUMN, event.type.name)
            put(EVENT_DESCRIPTION_COLUMN, event.description)
            put(EVENT_DATE_COLUMN, event.date)
            put(EVENT_TIME_COLUMN, event.time)
            put(PET_NAME_COLUMN, petName)
        }.let {
            petDatabase.insert(EVENT_TABLE, null, it)
        }
    }

    override fun findEventsByPet(petName: String): MutableList<Event> {
        val events = mutableListOf<Event>()
        val cursor = petDatabase.query(
            EVENT_TABLE, null, "$PET_NAME_COLUMN = ?", arrayOf(petName),
            null, null, null
        )
        while (cursor.moveToNext()) {
            events.add(cursorToEvent(cursor))
        }
        return events
    }

    override fun updateEvent(event: Event) = petDatabase.update(
        EVENT_TABLE,
        ContentValues().apply {
            put(EVENT_TYPE_COLUMN, event.type.name)
            put(EVENT_DESCRIPTION_COLUMN, event.description)
            put(EVENT_DATE_COLUMN, event.date)
            put(EVENT_TIME_COLUMN, event.time)
        },
        "$EVENT_ID_COLUMN = ?",
        arrayOf(event.id.toString())
    )

    override fun deleteEvent(eventId: Long) = petDatabase.delete(
        EVENT_TABLE, "$EVENT_ID_COLUMN = ?", arrayOf(eventId.toString())
    )

    private fun petToContentValue(pet: Pet) = ContentValues().apply {
        with(pet) {
            put(NAME_COLUMN, name)
            put(BIRTH_DATE_COLUMN, birthDate)
            put(TYPE_COLUMN, type.name)
            put(COLOR_COLUMN, color)
            put(SIZE_COLUMN, size.name)
        }
    }

    private fun cursorToPet(cursor: Cursor) = with(cursor) {
        Pet(
            getString(getColumnIndexOrThrow(NAME_COLUMN)),
            getString(getColumnIndexOrThrow(BIRTH_DATE_COLUMN)),
            PetType.valueOf(getString(getColumnIndexOrThrow(TYPE_COLUMN))),
            getString(getColumnIndexOrThrow(COLOR_COLUMN)),
            Size.valueOf(getString(getColumnIndexOrThrow(SIZE_COLUMN)))
        )
    }

    private fun cursorToEvent(cursor: Cursor) = with(cursor) {
        Event(
            id = getLong(getColumnIndexOrThrow(EVENT_ID_COLUMN)),
            type = EventType.valueOf(getString(getColumnIndexOrThrow(EVENT_TYPE_COLUMN))),
            description = getString(getColumnIndexOrThrow(EVENT_DESCRIPTION_COLUMN)),
            date = getString(getColumnIndexOrThrow(EVENT_DATE_COLUMN)),
            time = getString(getColumnIndexOrThrow(EVENT_TIME_COLUMN))
        )
    }
}
