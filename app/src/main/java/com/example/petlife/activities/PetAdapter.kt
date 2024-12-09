package com.example.petlife.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petlife.R
import com.example.petlife.model.pet.Pet

// PetAdapter.kt
class PetAdapter(private val petList: List<Pet>) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    // ViewHolder para representar cada item da lista
    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val petNameTextView: TextView = itemView.findViewById(R.id.nameItemTv)
        val petTypeTextView: TextView = itemView.findViewById(R.id.typeItemTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tile_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]
        holder.petNameTextView.text = pet.name
        holder.petTypeTextView.text = pet.type.name
    }

    override fun getItemCount(): Int {
        return petList.size
    }
}
