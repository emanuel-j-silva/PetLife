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

class PetSqliteImpl(context: Context): PetDAO {

    companion object{
        private const val PET_DATABASE_FILE = "pet"
        private const val PET_TABLE = "pet"
        private const val NAME_COLUMN = "name"
        private const val BIRTH_DATE_COLUMN = "birth_date"
        private const val TYPE_COLUMN = "type"
        private const val COLOR_COLUMN = "color"
        private const val SIZE_COLUMN = "size"

        private const val CREATE_PET_TABLE_STATEMENT =
            "CREATE TABLE IF NOT EXISTS $PET_TABLE(" +
                    "$NAME_COLUMN TEXT NOT NULL PRIMARY KEY," +
                    "$BIRTH_DATE_COLUMN TEXT NOT NULL," +
                    "$TYPE_COLUMN TEXT NOT NULL," +
                    "$COLOR_COLUMN TEXT NOT NULL," +
                    "$SIZE_COLUMN TEXT NOT NULL);"


    }

    private val petDatabase: SQLiteDatabase = context.openOrCreateDatabase(
        PET_DATABASE_FILE, MODE_PRIVATE, null)

    init {
        try {
            petDatabase.execSQL(CREATE_PET_TABLE_STATEMENT)
        }catch (se: SQLException){
            Log.e(context.getString(R.string.app_name),se.toString())
        }
    }

    override fun createPet(pet: Pet) =
        petDatabase.insert(PET_TABLE,null,petToContentValue(pet))

    override fun findPet(name: String): Pet {
        val cursor = petDatabase.query(
            true, PET_TABLE,null,"$NAME_COLUMN = ?", arrayOf(name),
            null,null,null,null
        )
        return if (cursor.moveToFirst()){
            cursorToPet(cursor)
        }else{
            Pet()
        }
    }

    override fun findAllPets(): MutableList<Pet> {
        TODO("Not yet implemented")
    }

    override fun updatePet(pet: Pet): Int {
        TODO("Not yet implemented")
    }

    override fun deletePet(name: String): Int {
        TODO("Not yet implemented")
    }

    private fun petToContentValue(pet: Pet) = ContentValues().apply {
        with(pet) {
            put(NAME_COLUMN, name)
            put(BIRTH_DATE_COLUMN, birthDate)
            put(TYPE_COLUMN, type.name)
            put(COLOR_COLUMN, color)
            put(SIZE_COLUMN, size.name)
        }
    }

    private fun cursorToPet(cursor: Cursor) = with(cursor){
        Pet(
            getString(getColumnIndexOrThrow(NAME_COLUMN)),
            getString(getColumnIndexOrThrow(BIRTH_DATE_COLUMN)),
            PetType.valueOf(getString(getColumnIndexOrThrow(TYPE_COLUMN))),
            getString(getColumnIndexOrThrow(COLOR_COLUMN)),
            Size.valueOf(getString(getColumnIndexOrThrow(SIZE_COLUMN)))
        )
    }

}