package com.openclassrooms.realestatemanager.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.openclassrooms.realestatemanager.R

class DetailActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val price = intent.getIntExtra("EXTRA_PRICE", 0)
        val type = intent.getStringExtra("EXTRA_TYPE")
        val area = intent.getIntExtra("EXTRA_AREA", 0)
        val location = intent.getStringExtra("EXTRA_LOCATION")

        findViewById<TextView>(R.id.priceTextView).text = price.toString() + "€"
        findViewById<TextView>(R.id.typeDetailPhoneTextView).text = type
        findViewById<TextView>(R.id.surfaceDetailPhoneTextView).text = area.toString()
        findViewById<TextView>(R.id.locationDetailPhoneTextView).text = location
        // Configurez d'autres vues si nécessaire
    }
}
