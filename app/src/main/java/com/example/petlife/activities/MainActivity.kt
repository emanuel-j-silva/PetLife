package com.example.petlife.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.petlife.R
import com.example.petlife.databinding.ActivityMainBinding
import com.example.petlife.pet.Pet
import com.example.petlife.pet.Size
import com.example.petlife.pet.Type

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var petLauncher:ActivityResultLauncher<Intent>
    private lateinit var vetLauncher:ActivityResultLauncher<Intent>
    private lateinit var vacLauncher:ActivityResultLauncher<Intent>
    private lateinit var shopLauncher:ActivityResultLauncher<Intent>


    private var pet: Pet? = null
    private var lastVeterinarianVisit: String? = null
    private var lastVaccination: String? = null
    private var lastPetShopVisit: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        val toolbar: Toolbar = findViewById(amb.toolbarTb.id)
        setSupportActionBar(toolbar)

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
                        size = Size.valueOf(size)
                    )
                    pet?.let { updatePetUi(it) }
                }
            }
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

        vacLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                result.data?.let { data->
                    val lastVac = data.getStringExtra("lastVaccination")
                    lastVaccination = lastVac
                }
                lastVaccination?.let { amb.lastVaccinationTv.text = lastVaccination }
            }
        }

        shopLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK){
                result.data?.let { data->
                    val lastShop = data.getStringExtra("lastPetShopVisit")
                    lastPetShopVisit = lastShop
                }
                lastPetShopVisit?.let { amb.lastPetshopTv.text = lastPetShopVisit }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editPetInfoMi -> {
                val intent = Intent(this, EditPetActivity::class.java).apply{
                    putExtra("name", pet?.name ?: "")
                    putExtra("birthDate", pet?.birthDate ?: "")
                    putExtra("type", pet?.type?.name ?: Type.DOG.name)
                    putExtra("color", pet?.color ?: "")
                    putExtra("size", pet?.size?.name ?: Size.MEDIUM.name)
                }
                petLauncher.launch(intent)
                true
            }
            R.id.editLastVaccinationMi -> {
                val intent = Intent(this, LastVacActivity::class.java).apply {
                    putExtra("lastVaccination", lastVaccination)
                }
                vacLauncher.launch(intent)
                true
            }
            R.id.editLastVeterinarianMi -> {
                val intent = Intent(this, LastVetActivity::class.java).apply {
                    putExtra("lastVeterinarianVisit", lastVeterinarianVisit)
                }
                vetLauncher.launch(intent)
                true
            }
            R.id.editLastPetShopMi -> {
                val intent = Intent(this, LastPetShopActivity::class.java).apply {
                    putExtra("lastPetShopVisit", lastPetShopVisit)
                }
                shopLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
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