package com.example.petlife.activities

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.petlife.R
import com.example.petlife.databinding.TilePetBinding
import com.example.petlife.model.pet.Pet

class PetAdapter(context: Context, private val petList: MutableList<Pet>
): ArrayAdapter<Pet>(context, R.layout.tile_pet){

    private data class PetTileHolder(
        val nameTv: TextView,
        val typeTv: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var tpb: TilePetBinding

        val pet = petList[position]

        var petTile = convertView
        if (petTile == null){
            tpb  = TilePetBinding.inflate(context.getSystemService(LAYOUT_INFLATER_SERVICE) as
                    LayoutInflater, parent, false)

            petTile = tpb.root
            val newPetTileHolder = PetTileHolder(tpb.nameTv, tpb.typeTv)

            petTile.tag = newPetTileHolder
        }

        val holder = petTile.tag as PetTileHolder
        holder.let {
            it.nameTv.text = pet.name
            it.typeTv.text = pet.type.toString()
        }

        return petTile
    }
}