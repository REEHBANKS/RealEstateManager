package com.openclassrooms.realestatemanager.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.openclassrooms.realestatemanager.R

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Récupérer le String passé dans l'intent
        val detailString = intent.getStringExtra("EXTRA_DETAIL")

        // Utiliser le String pour configurer le TextView
        findViewById<TextView>(R.id.detailTextView).text = detailString
    }
}
