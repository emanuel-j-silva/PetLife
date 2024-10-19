package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var parl:ActivityResultLauncher<Intent>
    private var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == RESULT_OK){
                result.data?.let { data->
                    val name = data.getStringExtra("name") ?: ""
                    val birthDate = data.getStringExtra("birthDate") ?: ""
                    val type = data.getStringExtra("type") ?: ""
                    val color = data.getStringExtra("color") ?: ""
                    val size = data.getStringExtra("size") ?: ""

                    pet = Pet(
                        name = name,
                        birthDate = birthDate,
                        type = Type.valueOf(type),
                        color = color,
                        size = Size.valueOf(size)
                    )
                    pet?.let { updatePetUi(it) }
                }
            }
        }

        amb.altPetInfoTv.setOnClickListener {
            val intent = Intent(this,EditPetActivity::class.java).apply{
                putExtra("name", pet?.name ?: "")
                putExtra("birthDate", pet?.birthDate ?: "")
                putExtra("type", pet?.type ?: Type.DOG.name)
                putExtra("color", pet?.color ?: "")
                putExtra("size", pet?.size ?: Size.MEDIUM.name)
            }
            parl.launch(intent)
        }
    }

    private fun updatePetUi(pet: Pet){
        amb.nameTv.text = pet.name
        amb.birthTv.text = pet.birthDate
        amb.typeTv.text = pet.type.name
        amb.colorTv.text = pet.color
        amb.sizeTv.text = pet.size.name
    }
}