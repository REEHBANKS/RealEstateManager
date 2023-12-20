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
import com.openclassrooms.realestatemanager.view.fragment.IntroDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val isTabletMode = findViewById<View>(R.id.detail_container) != null




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        val editToHide = menu?.findItem(R.id.action_edit)
        val addToHide = menu?.findItem(R.id.action_new_add)

        editToHide?.isVisible = false
        addToHide?.isVisible = false


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



}
