package com.example.petlife.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditPetBinding
import com.example.petlife.model.Constant.PET
import com.example.petlife.model.Constant.VIEW_MODE
import com.example.petlife.model.pet.Pet
import com.example.petlife.model.pet.Size
import com.example.petlife.model.pet.PetType

class EditPetActivity : AppCompatActivity() {
    private val apb: ActivityEditPetBinding by lazy {
        ActivityEditPetBinding.inflate(layoutInflater)
    }

    private lateinit var typeAdapter: ArrayAdapter<PetType>
    private lateinit var sizeAdapter: ArrayAdapter<Size>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(apb.root)

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)

        typeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item,
            PetType.entries)
        typeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        apb.typeSp.adapter = typeAdapter

        sizeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item,
            Size.entries)
        sizeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        apb.sizeSp.adapter = sizeAdapter

        val receivedPet = intent.getParcelableExtra<Pet>(PET)
        receivedPet?.let { pet ->
            with(apb){
                with(pet){
                    nameEt.isEnabled = !viewMode
                    birthEt.isEnabled = !viewMode
                    typeSp.isEnabled = !viewMode
                    colorEt.isEnabled = !viewMode
                    sizeSp.isEnabled = !viewMode

                    nameEt.setText(name)
                    nameEt.isEnabled = false
                    birthEt.setText(birthDate)
                    val typePosition = typeAdapter.getPosition(pet.type)
                    typeSp.setSelection(typePosition)
                    colorEt.setText(color)
                    val sizePosition = sizeAdapter.getPosition(pet.size)
                    sizeSp.setSelection(sizePosition)

                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        apb.toolbarTb.toolbar.let {
            it.subtitle = if (receivedPet == null)
                "New Pet"
            else
                if (viewMode)
                    "Pet details"
                else
                    "Edit Pet"

            setSupportActionBar(it)
        }

       apb.run {
           saveBt.setOnClickListener{
               Pet(
                   name = nameEt.text.toString(),
                   birthDate = birthEt.text.toString(),
                   type = PetType.valueOf(typeSp.selectedItem.toString()),
                   color = colorEt.text.toString(),
                   size = Size.valueOf(sizeSp.selectedItem.toString())
               ).let { pet ->
                   Intent().apply {
                       putExtra(PET, pet)
                       setResult(RESULT_OK,this)
                       finish()
                   }
               }
           }
       }

    }
}