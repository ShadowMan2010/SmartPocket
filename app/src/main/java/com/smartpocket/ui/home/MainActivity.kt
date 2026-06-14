package com.smartpocket.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smartpocket.R
import com.smartpocket.databinding.ActivityMainBinding
import com.smartpocket.service.SmartPocketService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { /* handle results in each fragment */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        requestRequiredPermissions()
        startSecurityService()

        // Animate bottom nav on first launch
        binding.bottomNav.post {
            binding.bottomNav.translationY = 200f
            binding.bottomNav.animate()
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(200)
                .start()
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == navController.currentDestination?.id) {
                false
            } else {
                val options = NavOptions.Builder()
                    .setPopUpTo(navController.graph.startDestinationId, true)
                    .setLaunchSingleTop(true)
                    .setRestoreState(true)
                    .build()
                navController.navigate(item.itemId, null, options)
                true
            }
        }
    }

    private fun requestRequiredPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val toRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (toRequest.isNotEmpty()) permissionLauncher.launch(toRequest.toTypedArray())
    }

    private fun startSecurityService() {
        val intent = Intent(this, SmartPocketService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    fun hideBottomNav() {
        binding.bottomNav.animate()
            .translationY(binding.bottomNav.height.toFloat())
            .setDuration(300)
            .withEndAction { binding.bottomNav.visibility = View.GONE }
            .start()
    }

    fun showBottomNav() {
        binding.bottomNav.visibility = View.VISIBLE
        binding.bottomNav.animate()
            .translationY(0f)
            .setDuration(300)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Service continues running; this is intentional
    }
}
