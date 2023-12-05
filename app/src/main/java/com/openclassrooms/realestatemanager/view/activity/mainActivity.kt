package com.openclassrooms.realestatemanager.view.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.openclassrooms.realestatemanager.view.fragment.DetailFragment
import com.openclassrooms.realestatemanager.view.fragment.IntroDetailFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurez la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Vérifier si le conteneur de détails existe dans la mise en page
        val isTabletMode = findViewById<View>(R.id.detail_container) != null
        Log.d("RENEL", "Mode tablette: $isTabletMode")


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestinationId = navController.currentDestination?.id
        val switchFragmentItem = menu?.findItem(R.id.action_switch_fragment)
        if (currentDestinationId == R.id.listRealEstatePropertyFragment2) {
            switchFragmentItem?.setIcon(R.drawable.baseline_map_24) // Set to map icon
        } else if (currentDestinationId == R.id.mapRealEstatePropertyFragment) {
            switchFragmentItem?.setIcon(R.drawable.baseline_format_list_bulleted_24) // Set to list icon
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_switch_fragment -> {
                val navController = findNavController(R.id.nav_host_fragment)
                val currentDestinationId = navController.currentDestination?.id
                if (currentDestinationId == R.id.listRealEstatePropertyFragment2) {
                    navController.navigate(R.id.action_listRealEstatePropertyFragment2_to_mapRealEstatePropertyFragment)
                    item.setIcon(R.drawable.baseline_format_list_bulleted_24)
                } else if (currentDestinationId == R.id.mapRealEstatePropertyFragment) {
                    navController.navigate(R.id.action_mapRealEstatePropertyFragment_to_listRealEstatePropertyFragment2)
                    item.setIcon(R.drawable.baseline_map_24)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        if (findViewById<View>(R.id.detail_container) != null) {
            // Chargez le fragment d'introduction au lieu de montrer un détail initial
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_container, IntroDetailFragment())
                .commit()
        }
    }


    /*   fun tryToShowDetailFragment(propertyName: String) {
           // Vérifiez si le conteneur de détails existe (pour s'assurer que c'est une tablette)
           if (findViewById<View>(R.id.detail_container) != null) {
               showDetailFragment(propertyName)
           }
       }
   */

    // Dans MainActivity.kt
    fun showDetailFragment(detailString: String) {
        val detailFragment = DetailFragment.newInstance(detailString)
        supportFragmentManager.beginTransaction()
            .replace(R.id.detail_container, detailFragment)
            .commit()
    }



}
