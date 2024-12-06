package com.example.petlife.activities

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.R
import com.example.petlife.databinding.ActivityMainBinding
import com.example.petlife.model.pet.Pet
import com.example.petlife.model.pet.Size
import com.example.petlife.model.pet.PetType

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var petLauncher: ActivityResultLauncher<Intent>

    private lateinit var piarl: ActivityResultLauncher<Intent>

    private var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        amb.toolbarIn.toolbar.let {
            it.subtitle = "Pet List"
            setSupportActionBar(it)
        }

        piarl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let {
                    startActivity(Intent(ACTION_VIEW, it))
                }
            }
        }

        petLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->
                        val name = data.getStringExtra("name") ?: ""
                        val birthDate = data.getStringExtra("birthDate") ?: ""
                        val type = data.getStringExtra("type") ?: ""
                        val color = data.getStringExtra("color") ?: ""
                        val size = data.getStringExtra("size") ?: ""

                        pet = Pet(
                            name = name,
                            birthDate = birthDate,
                            type = PetType.valueOf(type),
                            color = color,
                            size = Size.valueOf(size)
                        )
                        pet?.let { updatePetUi(it) }
                    }
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editItemMi -> {
                petLauncher.launch(Intent(this, EditPetActivity::class.java))
                true
            }

            R.id.editPetInfoMi -> {
                val intent = Intent(this, EditPetActivity::class.java).apply {
                    putExtra("name", pet?.name ?: "")
                    putExtra("birthDate", pet?.birthDate ?: "")
                    putExtra("type", pet?.type?.name ?: PetType.DOG.name)
                    putExtra("color", pet?.color ?: "")
                    putExtra("size", pet?.size?.name ?: Size.MEDIUM.name)
                }
                petLauncher.launch(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updatePetUi(pet: Pet) {
        amb.nameTv.text = pet.name
        amb.birthTv.text = pet.birthDate
        amb.typeTv.text = pet.type.name
        amb.colorTv.text = pet.color
        amb.sizeTv.text = pet.size.name
    }

}