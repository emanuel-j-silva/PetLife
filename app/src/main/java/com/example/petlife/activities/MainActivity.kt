package com.example.petlife.activities

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.R
import com.example.petlife.controller.MainController
import com.example.petlife.databinding.ActivityMainBinding
import com.example.petlife.model.Constant
import com.example.petlife.model.pet.Pet

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val petList: MutableList<Pet> = mutableListOf()

    private val petAdapter: PetAdapter by lazy {
        PetAdapter(this,petList)
    }

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
                    result.data?.getParcelableExtra<Pet>(Constant.PET)
                }else{
                    result.data?.getParcelableExtra(Constant.PET,Pet::class.java)
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

    private fun fillPetList(){
        Thread{
            runOnUiThread{
                petList.clear()
                petList.addAll(mainController.getPets())
                petAdapter.notifyDataSetChanged()
            }
        }.start()
    }

}