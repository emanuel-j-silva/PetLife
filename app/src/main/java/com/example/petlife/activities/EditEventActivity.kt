package com.example.petlife.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditEventBinding
import com.example.petlife.model.Constant.EVENT
import com.example.petlife.model.Constant.VIEW_MODE
import com.example.petlife.model.event.Event
import com.example.petlife.model.event.EventType

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

        aeb.typeSp.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {

                val event = (view as TextView).text.toString()
                if(event == "REMEDY"){
                    aeb.timeEt.visibility = VISIBLE
                    aeb.timeTv.visibility = VISIBLE
                }else{
                    aeb.timeEt.visibility = GONE
                    aeb.timeTv.visibility = GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                aeb.timeEt.visibility = GONE
                aeb.timeTv.visibility = GONE
            }
        }

        val receivedEvent = intent.getParcelableExtra<Event>(EVENT)
        receivedEvent?.let { event ->
            with(aeb){
                with(event){
                    typeSp.isEnabled = !viewMode
                    dateEt.isEnabled = !viewMode
                    descriptionEt.isEnabled = !viewMode
                    timeEt.isEnabled = !viewMode

                    val typePosition = typeAdapter.getPosition(event.type)
                    typeSp.setSelection(typePosition)
                    dateEt.setText(date)
                    descriptionEt.setText(description)
                    timeEt.setText(time)

                    timeEt.visibility = if(type == EventType.REMEDY) VISIBLE else GONE
                    timeTv. visibility = if(type == EventType.REMEDY) VISIBLE else GONE

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
            saveBt.setOnClickListener {
                val updatedEvent = receivedEvent?.copy(
                    type = EventType.valueOf(typeSp.selectedItem.toString()),
                    description = descriptionEt.text.toString(),
                    date = dateEt.text.toString(),
                    time = timeEt.text.toString()
                ) ?: Event(
                    type = EventType.valueOf(typeSp.selectedItem.toString()),
                    description = descriptionEt.text.toString(),
                    date = dateEt.text.toString(),
                    time = timeEt.text.toString()
                )

                Intent().apply {
                    putExtra(EVENT, updatedEvent)
                    setResult(RESULT_OK, this)
                    finish()
                }
            }
        }
    }
}