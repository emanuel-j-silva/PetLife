package com.example.petlife.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityLastPetShopBinding

class LastPetShopActivity : AppCompatActivity() {
    private val alb: ActivityLastPetShopBinding by lazy {
        ActivityLastPetShopBinding.inflate(layoutInflater)
    }
    private var lastPetShopVisit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        lastPetShopVisit = intent.getStringExtra("lastPetShopVisit")
        lastPetShopVisit?.let { fillEditField(it) }

        alb.saveBt.setOnClickListener{
            lastPetShopVisit = alb.textTv.text.toString()

            val resultIntent = Intent().apply {
                putExtra("lastPetShopVisit",lastPetShopVisit)
            }
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

    private fun fillEditField(string: String){
        alb.textTv.setText(string)
    }
}