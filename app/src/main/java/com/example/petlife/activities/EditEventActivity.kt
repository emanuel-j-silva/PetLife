package com.example.petlife.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditEventBinding
import com.example.petlife.model.Constant.EVENT
import com.example.petlife.model.Constant.PET
import com.example.petlife.model.Constant.VIEW_MODE
import com.example.petlife.model.event.Event
import com.example.petlife.model.event.EventType
import com.example.petlife.model.pet.Pet
import com.example.petlife.model.pet.PetType
import com.example.petlife.model.pet.Size

class EditEventActivity : AppCompatActivity() {
    private val aeb: ActivityEditEventBinding by lazy {
        ActivityEditEventBinding.inflate(layoutInflater)
    }

    private lateinit var typeAdapter: ArrayAdapter<EventType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aeb.root)

        val viewMode = intent.getBooleanExtra(VIEW_MODE, false)

        typeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item,
            EventType.entries)
        typeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        aeb.typeSp.adapter = typeAdapter

        val receivedEvent = intent.getParcelableExtra<Event>(EVENT)
        receivedEvent?.let { event ->
            with(aeb){
                with(event){
                    typeSp.isEnabled = !viewMode
                    dateEt.isEnabled = !viewMode
                    descriptionEt.isEnabled = !viewMode

                    val typePosition = typeAdapter.getPosition(event.type)
                    typeSp.setSelection(typePosition)
                    dateEt.setText(date)
                    descriptionEt.setText(description)

                    saveBt.visibility = if (viewMode) GONE else VISIBLE
                }
            }
        }

        aeb.toolbarTb.toolbar.let {
            it.subtitle = if (receivedEvent == null)
                "New Event"
            else
                if (viewMode)
                    "Event details"
                else
                    "Edit Event"

            setSupportActionBar(it)
        }

        aeb.run {
            saveBt.setOnClickListener{
                Event(
                    type = EventType.valueOf(typeSp.selectedItem.toString()),
                    description = descriptionEt.text.toString(),
                    date = dateEt.text.toString()
                    ).let { event ->
                    Intent().apply {
                        putExtra(EVENT, event)
                        setResult(RESULT_OK,this)
                        finish()
                    }
                }
            }
        }

    }
}