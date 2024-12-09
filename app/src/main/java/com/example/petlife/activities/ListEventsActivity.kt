package com.example.petlife.activities

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
import com.example.petlife.adapter.EventAdapter
import com.example.petlife.adapter.PetAdapter
import com.example.petlife.controller.EventController
import com.example.petlife.databinding.ActivityListEventsBinding
import com.example.petlife.model.Constant.EVENT
import com.example.petlife.model.Constant.PET
import com.example.petlife.model.Constant.VIEW_MODE
import com.example.petlife.model.event.Event
import com.example.petlife.model.pet.Pet

class ListEventsActivity : AppCompatActivity() {
    private val aleb: ActivityListEventsBinding by lazy {
        ActivityListEventsBinding.inflate(layoutInflater)
    }

    private var selectedPosition: Int = -1
    private val eventList: MutableList<Event> = mutableListOf()
    private lateinit var eventAdapter: EventAdapter

    private val eventController: EventController by lazy {
        EventController(this)
    }

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aleb.root)

        parl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val event = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                    result.data?.getParcelableExtra<Event>(EVENT)
                }else{
                    result.data?.getParcelableExtra(EVENT, Event::class.java)
                }
                event?.let { receivedEvent ->
                    val position = eventList.indexOfFirst { it.id == receivedEvent.id }
                    if (position == -1){
                        eventList.add(receivedEvent)
                        eventController.insertEvent(TODO(),receivedEvent)
                    }else{
                        eventList[position] = receivedEvent
                        eventController.updateEvent(receivedEvent)
                    }
                    eventAdapter.notifyDataSetChanged()
                }
            }
        }

        aleb.toolbarIn.toolbar.let {
            it.subtitle = "Events List"
            setSupportActionBar(it)
        }

        val recyclerView: RecyclerView = findViewById(R.id.eventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        eventAdapter = EventAdapter(
            eventList,
            onContextMenuRequested = { position ->
                selectedPosition = position
            },
            onItemClick = { position ->
                Intent(this, EditEventActivity::class.java).apply {
                    putExtra(EVENT, eventList[position])
                    putExtra(VIEW_MODE, true)
                    startActivity(this)
                }
            }
        )
        recyclerView.adapter = eventAdapter

        registerForContextMenu(recyclerView)

        fillEventList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editItemMi -> {
                parl.launch(Intent(this, EditEventActivity::class.java))
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
    ) = menuInflater.inflate(R.menu.context_event_menu, menu)

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editEventMi -> {
                val eventToEdit = eventList[selectedPosition]
                Intent(this, EditEventActivity::class.java).apply {
                    putExtra(EVENT, eventToEdit)
                    putExtra(VIEW_MODE, false)
                    parl.launch(this)
                }
                true
            }
            R.id.removeEventMi -> {
                val eventToRemove = eventList[selectedPosition]
                eventController.deleteEvent(eventToRemove.id)
                eventList.removeAt(selectedPosition)
                eventAdapter.notifyItemRemoved(selectedPosition)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun fillEventList(petName: String) {
        val allEvents = eventController.findEventsByPet(petName)
        eventList.clear()
        eventList.addAll(allEvents)
        eventAdapter.notifyDataSetChanged()
    }
}