package com.example.petlife

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.petlife.databinding.ActivityEditPetBinding
import com.example.petlife.databinding.ActivityLastVetBinding

class LastVetActivity : AppCompatActivity() {
    private val alb: ActivityLastVetBinding by lazy {
        ActivityLastVetBinding.inflate(layoutInflater)
    }
    private var lastVeterinarianVisit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        lastVeterinarianVisit = intent.getStringExtra("lastVeterinarianVisit")
        lastVeterinarianVisit?.let { fillEditField(it) }

        alb.saveBt.setOnClickListener{
            lastVeterinarianVisit = alb.textTv.text.toString()

            val resultIntent = Intent().apply {
                putExtra("lastVeterinarianVisit",lastVeterinarianVisit)
            }
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

    private fun fillEditField(string: String){
        alb.textTv.text = string
    }
}