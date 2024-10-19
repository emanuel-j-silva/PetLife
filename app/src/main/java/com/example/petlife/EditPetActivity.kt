package com.example.petlife

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditPetBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditPetActivity : AppCompatActivity() {
    private val apb: ActivityEditPetBinding by lazy {
        ActivityEditPetBinding.inflate(layoutInflater)
    }

    private var pet: Pet? = null

    private lateinit var typeAdapter: ArrayAdapter<Type>
    private lateinit var sizeAdapter: ArrayAdapter<Size>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        typeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item,
            Type.entries)
        typeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        apb.typeSp.adapter = typeAdapter

        sizeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item,
            Size.entries)
        sizeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        apb.sizeSp.adapter = sizeAdapter

        pet = Pet(
            name = intent.getStringExtra("name") ?: "",
            birthDate = intent.getStringExtra("birthDate") ?: "",
            type = intent.getStringExtra("type")?.let { Type.valueOf(it) } ?: Type.DOG,
            color = intent.getStringExtra("color") ?: "",
            size = intent.getStringExtra("size")?.let { Size.valueOf(it) } ?:Size.MEDIUM,
            lastPetShopVisit = intent.getStringExtra("lastPetShopVisit") ?: ""
        )

        pet?.let { fillEditFields(it) }

        apb.saveBt.setOnClickListener{
            pet = Pet(
                name = apb.nameEt.text.toString(),
                birthDate = apb.birthEt.text.toString(),
                type = Type.valueOf(apb.typeSp.selectedItem.toString()),
                color = apb.colorEt.text.toString(),
                size = Size.valueOf(apb.sizeSp.selectedItem.toString()),
                lastPetShopVisit = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            )

            val resultIntent = Intent().apply {
                putExtra("name", pet?.name)
                putExtra("birthDate", pet?.birthDate)
                putExtra("type", pet?.type?.name)
                putExtra("color", pet?.color)
                putExtra("size", pet?.size?.name)
            }
            setResult(RESULT_OK,resultIntent)
            finish()
        }

    }
    private fun fillEditFields(pet: Pet){
        apb.nameEt.setText(pet.name)
        apb.birthEt.setText(pet.birthDate)

        val typePosition = typeAdapter.getPosition(pet.type)
        apb.typeSp.setSelection(typePosition)

        apb.colorEt.setText(pet.color)

        val sizePosition = sizeAdapter.getPosition(pet.size)
        apb.sizeSp.setSelection(sizePosition)
    }
}