package com.example.petlife.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityEditEventBinding

class EditEventActivity : AppCompatActivity() {
    private val aeb: ActivityEditEventBinding by lazy {
        ActivityEditEventBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(aeb.root)
    }
}