package com.example.petlife.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlife.R
import com.example.petlife.controller.MainController
import com.example.petlife.databinding.ActivityMainBinding
import com.example.petlife.model.Constant.PET
import com.example.petlife.model.Constant.VIEW_MODE
import com.example.petlife.model.pet.Pet

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val petList: MutableList<Pet> = mutableListOf()

    private val mainController:MainController by lazy {
        MainController(this)
    }

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        val recyclerView: RecyclerView = findViewById(R.id.petsRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val pet = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                    result.data?.getParcelableExtra<Pet>(PET)
                }else{
                    result.data?.getParcelableExtra(PET,Pet::class.java)
                }
                pet?.let { receivedPet ->
                    val position = petList.indexOfFirst { it.name == receivedPet.name }
                    if (position == -1){
                        petList.add(receivedPet)
                        mainController.insertPet(receivedPet)
                    }else{
                        petList[position] = receivedPet
                        mainController.modifyPet(receivedPet)
                    }
                }
            }
        }

        amb.toolbarIn.toolbar.let {
            it.subtitle = "Pet List"
            setSupportActionBar(it)
        }

        fillPetList()

        val petAdapter = PetAdapter(petList)
        recyclerView.adapter = petAdapter

        registerForContextMenu(amb.petsRecyclerView)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editItemMi -> {
                parl.launch(Intent(this, EditPetActivity::class.java))
                true
            }

            else -> {
                false
            }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) = menuInflater.inflate(R.menu.context_menu_main, menu)


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
        return when(item.itemId){
            R.id.editPetMi -> {
                Intent(this,EditPetActivity::class.java).apply {
                    putExtra(PET,petList[position])
                    putExtra(VIEW_MODE,false)
                    parl.launch(this)
                }
                true
            }
            R.id.removePetMi -> {
                mainController.removePet(petList[position].name)
                petList.removeAt(position)
                true
            }
            else -> {
                false
            }
        }
    }


    private fun fillPetList() {
        val allPets = mainController.getPets()
        petList.clear()
        petList.addAll(allPets)
    }

}