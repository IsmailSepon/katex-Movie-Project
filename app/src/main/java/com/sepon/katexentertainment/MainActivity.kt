package com.sepon.katexentertainment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.sepon.katexentertainment.communicator.Communicator
import com.sepon.katexentertainment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , Communicator{

    private lateinit var binding: ActivityMainBinding
    var navView: BottomNavigationView? = null
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

         navView = binding.navView
        navView!!.itemIconTintList = null
        

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_notifications
            )
        )
        navView!!.setupWithNavController(navController)
    }

    override fun hideBottomNav() {
        navView!!.visibility  = View.GONE
    }

    override fun showBottomNav() {
        navView!!.visibility  = View.VISIBLE
    }
}