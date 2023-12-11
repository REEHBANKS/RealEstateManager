package com.openclassrooms.realestatemanager.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.openclassrooms.realestatemanager.R
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Récupérer les données passées dans l'intent
        val price = intent.getIntExtra("EXTRA_PRICE", 0)
        val type = intent.getStringExtra("EXTRA_TYPE") ?: ""
        val area = intent.getIntExtra("EXTRA_AREA", 0)
        val location = intent.getStringExtra("EXTRA_LOCATION") ?: ""
        val nearby = intent.getStringExtra("EXTRA_NEARBY") ?: ""
        val rooms = intent.getIntExtra("EXTRA_ROOMS", 0)
        val description = intent.getStringExtra("EXTRA_DESCRIPTION") ?: ""
        val isSold = intent.getBooleanExtra("EXTRA_IS_SOLD", false)
        val soldDate = intent.getStringExtra("EXTRA_SOLD_DATE") ?: ""

        // Utiliser les données pour configurer les TextViews
        findViewById<TextView>(R.id.priceTextView).text = getString(R.string.price_format, price)
        findViewById<TextView>(R.id.typeDetailPhoneTextView).text = type
        findViewById<TextView>(R.id.surfaceDetailPhoneTextView).text = getString( area)
        findViewById<TextView>(R.id.locationDetailPhoneTextView).text = location
        findViewById<TextView>(R.id.nearbyDetailPhoneTextView).text = nearby
        findViewById<TextView>(R.id.roomsDetailPhoneTextView).text = getString( rooms)
        findViewById<TextView>(R.id.descriptionDetailPhoneTextView).text = description
        val stateButton = findViewById<Button>(R.id.button_state)
        val dateSoldTextView = findViewById<TextView>(R.id.dateOfSoldDetailPhoneTextView)

        // Ajoutez d'autres configurations si nécessaire


        // Mettez à jour l'état du bouton et la date de vente en fonction de si la propriété est vendue
        if (isSold) {

            stateButton.apply {
                text = "SOLD"
                background = ContextCompat.getDrawable(this@DetailActivity, R.drawable.button_sold)
            }
            dateSoldTextView.text = soldDate
        } else {

            stateButton.apply {
                text = "FOR SALE"
                background = ContextCompat.getDrawable(this@DetailActivity, R.drawable.button_for_sale)
            }
            dateSoldTextView.text = ""
        }




    }
}
