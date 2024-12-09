package com.example.petlife.activities

import com.example.petlife.adapter.PetAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

    private var selectedPosition: Int = -1
    private val petList: MutableList<Pet> = mutableListOf()
    private lateinit var petAdapter: PetAdapter

    private val mainController:MainController by lazy {
        MainController(this)
    }

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

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
                    petAdapter.notifyDataSetChanged()
                }
            }
        }

        amb.toolbarIn.toolbar.let {
            it.subtitle = "Pet List"
            setSupportActionBar(it)
        }

        val recyclerView: RecyclerView = findViewById(R.id.petsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        petAdapter = PetAdapter(
            petList,
            onContextMenuRequested = { position ->
                selectedPosition = position
            },
            onItemClick = { position ->
                Intent(this, ListEventsActivity::class.java).apply {
                    putExtra(PET, petList[position].name)
                    startActivity(this)
                }
            }
        )
        recyclerView.adapter = petAdapter

        registerForContextMenu(recyclerView)

        fillPetList()

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
        return when (item.itemId) {
            R.id.editPetMi -> {
                val petToEdit = petList[selectedPosition]
                Intent(this, EditPetActivity::class.java).apply {
                    putExtra(PET, petToEdit)
                    putExtra(VIEW_MODE, false)
                    parl.launch(this)
                }
                true
            }
            R.id.removePetMi -> {
                val petToRemove = petList[selectedPosition]
                mainController.removePet(petToRemove.name)
                petList.removeAt(selectedPosition)
                petAdapter.notifyItemRemoved(selectedPosition)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun fillPetList() {
        val allPets = mainController.getPets()
        petList.clear()
        petList.addAll(allPets)
        petAdapter.notifyDataSetChanged()
    }

}