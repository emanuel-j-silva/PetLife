package com.example.petlife.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityLastVacBinding

class LastVacActivity : AppCompatActivity() {
    private val alb: ActivityLastVacBinding by lazy {
        ActivityLastVacBinding.inflate(layoutInflater)
    }
    private var lastVaccination: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        lastVaccination = intent.getStringExtra("lastVaccination")
        lastVaccination?.let { fillEditField(it) }

        alb.saveBt.setOnClickListener{
            lastVaccination = alb.textTv.text.toString()

            val resultIntent = Intent().apply {
                putExtra("lastVaccination",lastVaccination)
            }
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

    private fun fillEditField(string: String){
        alb.textTv.setText(string)
    }
}