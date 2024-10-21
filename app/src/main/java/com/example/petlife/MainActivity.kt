package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var petLauncher:ActivityResultLauncher<Intent>
    private lateinit var vetLauncher:ActivityResultLauncher<Intent>
    private lateinit var vacLauncher:ActivityResultLauncher<Intent>

    private var pet: Pet? = null
    private var lastVeterinarianVisit: String? = null
    private var lastVaccination: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        petLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                        size = Size.valueOf(size),
                        lastPetShopVisit = LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
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
                putExtra("lastPetShopVisit", pet?.lastPetShopVisit)
            }
            petLauncher.launch(intent)
        }

        vetLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                result.data?.let { data->
                    val lastVetVisit = data.getStringExtra("lastVeterinarianVisit")
                    lastVeterinarianVisit = lastVetVisit
                }
                lastVeterinarianVisit?.let { amb.lastVeterinarianTv.text = lastVeterinarianVisit }
            }
        }

        amb.altLastVeterinarianTv.setOnClickListener{
            val intent = Intent(this,LastVetActivity::class.java).apply {
                putExtra("lastVeterinarianVisit", lastVeterinarianVisit)
            }
            vetLauncher.launch(intent)
        }

        vacLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                result.data?.let { data->
                    val lastVac = data.getStringExtra("lastVaccination")
                    lastVaccination = lastVac
                }
                lastVaccination?.let { amb.lastVaccinationTv.text = lastVaccination }
            }
        }

        amb.altLastVaccinationTv.setOnClickListener{
            val intent = Intent(this,LastVacActivity::class.java).apply {
                putExtra("lastVaccination", lastVaccination)
            }
            vacLauncher.launch(intent)
        }

    }

    private fun updatePetUi(pet: Pet){
        amb.nameTv.text = pet.name
        amb.birthTv.text = pet.birthDate
        amb.typeTv.text = pet.type.name
        amb.colorTv.text = pet.color
        amb.sizeTv.text = pet.size.name
        amb.lastPetshopTv.text = pet.lastPetShopVisit
    }
}