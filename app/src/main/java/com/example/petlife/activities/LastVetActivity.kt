package com.example.petlife.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petlife.databinding.ActivityLastVetBinding

class LastVetActivity : AppCompatActivity() {
    private val alb: ActivityLastVetBinding by lazy {
        ActivityLastVetBinding.inflate(layoutInflater)
    }
    private var lastVeterinarianVisit: String? = null
    private var phone: String? = null
    private var site: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(alb.root)

        lastVeterinarianVisit = intent.getStringExtra("lastVeterinarianVisit")
        lastVeterinarianVisit?.let { alb.textTv.setText(lastVeterinarianVisit)}

        phone = intent.getStringExtra("phone")
        phone?.let { alb.telephoneEt.setText(phone) }

        site = intent.getStringExtra("site")
        site?.let { alb.siteEt.setText(site) }

        alb.saveBt.setOnClickListener{
            lastVeterinarianVisit = alb.textTv.text.toString()
            phone = alb.telephoneEt.text.toString()
            site = alb.siteEt.text.toString()

            val resultIntent = Intent().apply {
                putExtra("lastVeterinarianVisit",lastVeterinarianVisit)
                putExtra("phone",phone)
                putExtra("site",site)
            }
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

}