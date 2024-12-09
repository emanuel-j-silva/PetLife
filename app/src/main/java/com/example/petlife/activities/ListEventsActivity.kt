package com.example.petlife.activities

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petlife.R
import com.example.petlife.databinding.ActivityListEventsBinding
import com.example.petlife.model.event.Event

class ListEventsActivity : AppCompatActivity() {
    private val aleb: ActivityListEventsBinding by lazy {
        ActivityListEventsBinding.inflate(layoutInflater)
    }

    private var selectedPosition: Int = -1
    private val petList: MutableList<Event> = mutableListOf()

    private lateinit var parl: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aleb.root)

        aleb.toolbarIn.toolbar.let {
            it.subtitle = "Events List"
            setSupportActionBar(it)
        }

        val recyclerView: RecyclerView = findViewById(R.id.eventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
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
}