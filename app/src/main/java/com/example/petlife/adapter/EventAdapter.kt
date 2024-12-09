package com.example.petlife.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.petlife.databinding.TileEventBinding
import com.example.petlife.model.event.Event

class EventAdapter(
    private val eventsList: List<Event>,
    private val onContextMenuRequested: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(val binding: TileEventBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnCreateContextMenuListener {

        init {
            binding.root.setOnClickListener {
                onItemClick(adapterPosition)
            }

            binding.root.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
            onContextMenuRequested(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = TileEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventsList[position]
        holder.binding.eventTypeItemTv.text = event.type.name
        holder.binding.eventDateItemTv.text = event.date
    }

    override fun getItemCount(): Int = eventsList.size
}