package com.example.petlife.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.petlife.R
import com.example.petlife.model.pet.Pet
import java.sql.SQLException

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

    override fun createPet(pet: Pet): Long {
        TODO("Not yet implemented")
    }

    override fun findPet(name: String): Pet {
        TODO("Not yet implemented")
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
}